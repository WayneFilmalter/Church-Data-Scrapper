package churchDetailsFetcher;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import churchDetailsFetcher.dialog.ProgressBarDialog;
import churchDetailsFetcher.dialog.SuccessDialog;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;

public class WebScraper {

	public static void scrapeEmails(ChurchDataTableModel tableModel, Component parentComponent, Runnable onCompletion) {
		List<ChurchTableData> churches = tableModel.getTableData(); // Assuming you have a method to get church data

		WebDriverManager.chromedriver().setup();

		// Set up ChromeDriver in headless mode
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless"); // Run Chrome in headless mode
		options.addArguments("--disable-gpu");
		options.addArguments("--no-sandbox");
		WebDriver driver = new ChromeDriver(options);

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
					if (website != null && !website.isEmpty()) {
						try {
							driver.get(website); // Navigate to the website
							String pageSource = driver.getPageSource();
							List<String> emails = extractEmails(pageSource);
							if (!emails.isEmpty()) {
								church.setEmail(String.join(", ", emails)); // Update email in the church data
								found += 1;
								System.out.println("Found emails for " + church.getName() + ": " + emails);
							} else {
								System.out.println("No email found for " + church.getName());
							}
						} catch (Exception e) {
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
				progressBarDialog.dispose(); // Close the progress dialog when scraping is complete
				driver.quit(); // Close the browser

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

	private static List<String> extractEmails(String pageSource) {
		List<String> emails = new ArrayList<>();

		// Step 1: Find all email-like patterns in the page source
		extractEmailsFromText(pageSource, emails);

		// Return the list of found emails (could be empty if none were found)
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
		// Remove unwanted parts from the email
		email = email.split("[?=]")[0].trim(); // Keep only the part before any '?' or '='

		// Check if the cleaned email is not empty and contains an '@'
		if (!email.isEmpty() && email.contains("@") && !emails.contains(email)) {
			emails.add(email);
		}
	}
}