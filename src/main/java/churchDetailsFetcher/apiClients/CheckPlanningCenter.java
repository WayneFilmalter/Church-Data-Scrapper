package churchDetailsFetcher.apiClients;

import java.awt.Component;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import churchDetailsFetcher.dialog.ProgressBarDialog;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;

public class CheckPlanningCenter {

    public static void checkPlanningCenterLinks(ChurchDataTableModel tableModel, Component parentComponent) {
        List<ChurchTableData> churches = tableModel.getTableData();

        // Create and display the custom ProgressBarDialog
        ProgressBarDialog progressBarDialog = new ProgressBarDialog((JFrame) parentComponent, "Finding PCO Links");
        progressBarDialog.setProgressMax(churches.size());

        // Use SwingWorker to handle background scraping process
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                int progress = 0;
                int found = 0;

                for (ChurchTableData church : churches) {
                    if (progressBarDialog.isCancelled()) {
                        System.out.println("PCO checking canceled.");
                        break; // Exit the loop if PCO checking is canceled
                    }

                    String website = church.getWebsite();
                    if (website != null && !website.isEmpty()) {
                        try {
                            // First attempt using Jsoup
                            if (checkPlanningCenterWithJsoup(website)) {
                                church.setHasPCO(true); // Update the church data to indicate PCO is found
                                found++;
                                System.out.println("Found Planning Center link for " + church.getName());
                            } else {
                                System.out.println("No Planning Center link found with Jsoup for " + church.getName());
                                // If Jsoup doesn't find anything, try Playwright
                                if (checkPlanningCenterWithPlaywright(website)) {
                                    church.setHasPCO(true);
                                    found++;
                                    System.out.println(
                                            "Found Planning Center link for " + church.getName() + " using Playwright");
                                } else {
                                    System.out.println("No Planning Center link found for " + church.getName()
                                            + " using Playwright");
                                }
                            }
                        } catch (IOException e) {
                            System.out.println("Error connecting to " + website + ": " + e.getMessage());
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
                progressBarDialog.dispose(); // Close the progress dialog when PCO checking is complete

                if (!progressBarDialog.isCancelled()) {
                    // Show notification or handle completion
                    System.out.println("PCO checking completed.");
                }
                tableModel.setCheckPCOdone(true);
            }
        };

        worker.execute();
        progressBarDialog.setVisible(true); // Display the progress bar
    }

    // Method to check if the document contains Planning Center links using Jsoup
    private static boolean checkPlanningCenterWithJsoup(String website) throws IOException {
        Document doc = Jsoup.connect(website).get();
        return containsPlanningCenter(doc);
    }

    // Method to check if the document contains Planning Center links using
    // Playwright
    private static boolean checkPlanningCenterWithPlaywright(String website) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate(website, new Page.NavigateOptions().setTimeout(60000)); // 30 seconds timeout
            Document doc = Jsoup.parse(page.content()); // Convert Playwright page content to Jsoup Document

            boolean found = containsPlanningCenter(doc);
            context.close(); // Close the context after use
            browser.close(); // Close the browser
            return found;
        } catch (Exception e) {
            System.out.println("Error using Playwright to check " + website + ": " + e.getMessage());
            return false;
        }
    }

    // Method to check if the document contains Planning Center links
    private static boolean containsPlanningCenter(Document doc) {
        // Check for links that contain "planningcenter.com" or "churchcenter.com"
        for (Element link : doc.select("a[href]")) {
            String href = link.attr("href");
            if (href.contains("planningcenter.com") || href.contains("churchcenter.com")) {
                return true; // Found a link to Planning Center
            }
        }
        return false; // No Planning Center links found
    }
}