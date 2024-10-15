package churchDetailsFetcher;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import churchDetailsFetcher.dialog.ProgressBarDialog;
import churchDetailsFetcher.dialog.SuccessDialog;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;

public class WebScraper {

	public static void scrapeEmails(ChurchDataTableModel tableModel, Component parentComponent) {

		List<ChurchTableData> churches = tableModel.getTableData(); // Assuming you have a method to get church data

		// Create and display the custom ProgressBarDialog
		ProgressBarDialog progressBarDialog = new ProgressBarDialog((JFrame) parentComponent);
		progressBarDialog.setProgressMax(churches.size());

		// Use SwingWorker to handle background scraping process
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				int progress = 0;

				for (ChurchTableData church : churches) {
					if (progressBarDialog.isCancelled()) {
						System.out.println("Scraping canceled.");
						break; // Exit the loop if scraping is canceled
					}

					String website = church.getWebsite();
					if (website != null && !website.isEmpty()) {
						try {
							Document doc = Jsoup.connect(website).get();
							List<String> emails = extractEmails(doc);
							if (!emails.isEmpty()) {
								church.setEmail(String.join(", ", emails)); // Update email in the church data
								System.out.println("Found emails for " + church.getName() + ": " + emails);
							} else {
								System.out.println("No email found for " + church.getName());
							}
						} catch (IOException e) {
							System.out.println("Error connecting to " + website + ": " + e.getMessage());
						}
					} else {
						System.out.println("No website provided for " + church.getName());
					}

					// Update progress bar
					progress++;
					progressBarDialog.updateProgress(progress);
				}

				return null;
			}

			@Override
			protected void done() {
			    progressBarDialog.dispose(); // Close the progress dialog when scraping is complete
			    
			    if (!progressBarDialog.isCancelled()) {
			        // Show the notification for 5 seconds
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
	    for (Element element : doc.select("a[href^=mailto:]")) {
	        String email = element.attr("href").replace("mailto:", "").trim();
	        if (!emails.contains(email)) {
	            emails.add(email);
	        }
	    }

	    // If we found emails using mailto, return them
	    if (!emails.isEmpty()) {
	        return emails;
	    }

	    // Step 2: Check for email-like patterns in the entire document text
	    String text = doc.text();
	    Pattern emailPattern = Pattern.compile(
	        "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
	    Matcher matcher = emailPattern.matcher(text);

	    while (matcher.find()) {
	        String email = matcher.group();
	        if (!emails.contains(email)) {
	            emails.add(email);
	        }
	    }

	    // If we found emails from the text pattern, return them
	    if (!emails.isEmpty()) {
	        return emails;
	    }

	    // Step 3: Check for 'email:' patterns in the document text
	    Matcher emailLinkMatcher = Pattern.compile("email:\\s*([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})").matcher(text);
	    while (emailLinkMatcher.find()) {
	        String email = emailLinkMatcher.group(1);
	        if (!emails.contains(email)) {
	            emails.add(email);
	        }
	    }

	    // If we found emails using the email: pattern, return them
	    if (!emails.isEmpty()) {
	        return emails;
	    }

	    // Step 4: Finally, find any potential emails in the text again (using regex)
	    matcher = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}").matcher(text);
	    while (matcher.find()) {
	        String email = matcher.group();
	        if (!emails.contains(email)) {
	            emails.add(email);
	        }
	    }

	    // Return the list of found emails (could be empty if none were found)
	    return emails;
	}
}