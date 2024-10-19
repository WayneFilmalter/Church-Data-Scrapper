package churchDetailsFetcher.apiClients;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.impl.TargetClosedError;

import churchDetailsFetcher.dialog.ProgressBarDialog;
import churchDetailsFetcher.dialog.SuccessDialog;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;

public class HeadlessWebScraper {

    public static void scrapeEmails(ChurchDataTableModel tableModel, Component parentComponent, Runnable onCompletion) {
        List<ChurchTableData> churches = tableModel.getTableData();
        // Filter churches without an email
        List<ChurchTableData> churchesToScrape = new ArrayList<>();
        for (ChurchTableData church : churches) {
            if (!church.getHasEmail()) {
                churchesToScrape.add(church);
            }
        }

        ProgressBarDialog progressBarDialog = new ProgressBarDialog((JFrame) parentComponent, "Scraping for emails...");
        progressBarDialog.setProgressMax(churchesToScrape.size());

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() {
                int progress = 0;
                int found = 0;

                try (Playwright playwright = Playwright.create()) {
                    Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

                    for (ChurchTableData church : churchesToScrape) {
                        if (progressBarDialog.isCancelled()) {
                            System.out.println("Scraping canceled.");
                            break;
                        }

                        String website = church.getWebsite();
                        if (website != null && !website.isEmpty()) {
                            try {
                                // Create a new context and page for each church to avoid closed target issues
                                BrowserContext context = browser.newContext();
                                Page page = context.newPage();

                                page.navigate(website, new Page.NavigateOptions().setTimeout(90000)); // 30 seconds
                                                                                                      // timeout
                                String pageSource = page.content();
                                List<String> emails = extractEmails(pageSource);

                                if (!emails.isEmpty()) {
                                    church.setEmail(String.join(", ", emails));
                                    church.setHasEmail(true);
                                    found++;
                                    System.out.println("Found emails for " + church.getName() + ": " + emails);
                                } else {
                                    System.out.println("No email found for " + church.getName());
                                }

                                context.close(); // Close the context after use

                            } catch (TimeoutError te) {
                                String errorMessage = "Timeout while connecting to " + website + ": " + te.getMessage();
                                logError(errorMessage);

                            } catch (TargetClosedError tce) {
                                String errorMessage = "Error connecting to " + website
                                        + ": Browser or page closed unexpectedly: " + tce.getMessage();
                                logError(errorMessage);

                            } catch (Exception e) {
                                String errorMessage = "Error connecting to " + website + ": " + e.getMessage();
                                logError(errorMessage);
                            }
                        } else {
                            System.out.println("No website provided for " + church.getName());
                        }

                        progress++;
                        progressBarDialog.updateProgress(progress, found, churchesToScrape.size());

                        try {
                            Thread.sleep(1000); // Avoid getting blocked by websites
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Sleep interrupted: " + e.getMessage());
                        }
                    }

                    browser.close();
                } catch (Exception e) {
                    System.out.println("Error during scraping: " + e.getMessage());
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
                    SuccessDialog.showNotificationDialog(parentComponent, "Scraping found some data");
                }
            }
        };

        worker.execute();
        progressBarDialog.setVisible(true);
    }

    private static void logError(String message) {
        // Log the error message (could be written to a file or console)
        System.err.println(message); // Log to console for now; consider using a logging framework
    }

    private static List<String> extractEmails(String pageSource) {
        List<String> emails = new ArrayList<>();
        extractEmailsFromText(pageSource, emails);
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
}