package churchDetailsFetcher.apiClients;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import churchDetailsFetcher.dialog.ProgressBarDialog;
import churchDetailsFetcher.dialog.SuccessDialog;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;

public class StaticWebScraper {

    public static void scrapeEmails(ChurchDataTableModel tableModel, Component parentComponent, Runnable onCompletion) {
        List<ChurchTableData> churches = tableModel.getTableData();
        ProgressBarDialog progressBarDialog = new ProgressBarDialog((JFrame) parentComponent, "Scraping for emails...");
        progressBarDialog.setProgressMax(churches.size());

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                int[] progress = { 0 };
                int[] found = { 0 };

                for (ChurchTableData church : churches) {
                    if (progressBarDialog.isCancelled()) {
                        System.out.println("Scraping canceled.");
                        break;
                    }

                    executorService.submit(() -> {
                        String website = church.getWebsite();
                        if (website != null && !website.isEmpty() && !church.getHasEmail()) {
                            try {
                                Document doc = Jsoup.connect(website).get();
                                List<String> emails = extractEmails(doc);
                                if (!emails.isEmpty()) {
                                    church.setEmail(String.join(", ", emails));
                                    church.setHasEmail(true);
                                    synchronized (found) {
                                        found[0]++;
                                    }
                                    System.out.println("Found emails for " + church.getName() + ": " + emails);
                                } else {
                                    System.out.println("No email found for " + church.getName());
                                }
                            } catch (IOException e) {
                                logError("Error connecting to " + website + ": " + e.getMessage());
                            }
                        } else {
                            System.out.println("No website provided for " + church.getName());
                        }

                        synchronized (progress) {
                            progress[0]++;
                            progressBarDialog.updateProgress(progress[0], found[0], churches.size());
                        }
                    });
                }

                executorService.shutdown();
                try {
                    executorService.awaitTermination(60, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void done() {
                progressBarDialog.dispose();
                if (!progressBarDialog.isCancelled()) {
                    if (onCompletion != null) {
                        onCompletion.run();
                    }
                    SuccessDialog.showNotificationDialog(parentComponent, "Scraping completed successfully.");
                }
            }
        };

        worker.execute();
        progressBarDialog.setVisible(true);
    }

    private static List<String> extractEmails(Document doc) {
        List<String> emails = new ArrayList<>();
        emails.addAll(extractEmailsFromMailto(doc));
        if (!emails.isEmpty()) {
            return emails;
        }

        String text = doc.text();
        extractEmailsFromText(text, emails);
        return emails;
    }

    private static List<String> extractEmailsFromMailto(Document doc) {
        List<String> emails = new ArrayList<>();
        for (var element : doc.select("a[href^=mailto:]")) {
            String email = element.attr("href").replace("mailto:", "").trim();
            email = email.split("[?=]")[0].trim();
            cleanEmail(email, emails);
        }
        return emails;
    }

    private static void extractEmailsFromText(String text, List<String> emails) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
        Matcher matcher = emailPattern.matcher(text);
        while (matcher.find()) {
            String email = matcher.group();
            cleanEmail(email, emails);
        }
    }

    private static void cleanEmail(String email, List<String> emails) {
        email = email.split("[?=]")[0].trim();
        if (!email.isEmpty() && email.contains("@") && !emails.contains(email)) {
            emails.add(email);
        }
    }

    private static void logError(String message) {
        System.err.println(message);
    }
}