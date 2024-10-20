package churchDetailsFetcher.apiClients;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        List<ChurchTableData> churches = tableModel.getTableData(); // Assuming you have a method to get church data

        // Create and display the custom ProgressBarDialog
        ProgressBarDialog progressBarDialog = new ProgressBarDialog((JFrame) parentComponent, "Scraping for emails...");
        progressBarDialog.setProgressMax(churches.size());

        // Use SwingWorker to handle background scraping process
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                int progress = 0;
                int found = 0;

                for (ChurchTableData church : churches) {
                    if (progressBarDialog.isCancelled()) {
                        System.out.println("Scraping canceled.");
                        break; // Exit the loop if scraping is canceled
                    }

                    String website = church.getWebsite();
                    if (website != null && !website.isEmpty() && !church.getHasEmail()) {
                        try {
                            Document doc = Jsoup.connect(website).get();
                            List<String> emails = extractEmails(doc);
                            if (!emails.isEmpty()) {
                                church.setEmail(String.join(", ", emails)); // Update email in the church data
                                church.setHasEmail(true);
                                found++;
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

                    // Update progress bar
                    progress++;
                    progressBarDialog.updateProgress(progress, found, churches.size());
                }

                return null;
            }

            @Override
            protected void done() {
                progressBarDialog.dispose(); // Close the progress dialog when scraping is complete

                if (!progressBarDialog.isCancelled()) {
                    if (onCompletion != null) {
                        onCompletion.run(); // Trigger the PCO check
                    }
                    SuccessDialog.showNotificationDialog(parentComponent, "Scraping found some data");
                }
            }
        };

        worker.execute();
        progressBarDialog.setVisible(true); // Display the progress bar
    }

    private static List<String> extractEmails(Document doc) {
        List<String> emails = new ArrayList<>();

        // Step 1: Find all mailto links
        emails.addAll(extractEmailsFromMailto(doc));

        // If we found emails using mailto, return them
        if (!emails.isEmpty()) {
            return emails;
        }

        // Step 2: Check for email-like patterns in the entire document text
        String text = doc.text();
        extractEmailsFromText(text, emails);

        // Return the list of found emails (could be empty if none were found)
        return emails;
    }

    private static List<String> extractEmailsFromMailto(Document doc) {
        List<String> emails = new ArrayList<>();
        for (var element : doc.select("a[href^=mailto:]")) {
            String email = element.attr("href").replace("mailto:", "").trim();
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
        // Log the error message to the console or a log file
        System.err.println(message); // Change this to logging to a file if needed
    }
}