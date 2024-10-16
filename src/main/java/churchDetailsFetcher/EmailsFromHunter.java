package churchDetailsFetcher;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import okhttp3.Call; // this imports for okhttp3 is complaining
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import churchDetailsFetcher.dialog.ProgressBarDialog;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;
import churchDetailsFetcher.types.HunterData;

public class EmailsFromHunter {

	public static void getEmailsFromHunter(ChurchDataTableModel tableModel, Component parentComponent,
			Runnable onCompletion) {

		List<ChurchTableData> churches = tableModel.getTableData();
		ProgressBarDialog progressBarDialog = new ProgressBarDialog((JFrame) parentComponent,
				"Fetching emails from Hunter...");
		progressBarDialog.setProgressMax(churches.size());

		final int[] progress = { 0 };

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				for (ChurchTableData church : churches) {
					if (progressBarDialog.isCancelled()) {
						System.out.println("Fetching canceled.");
						break; // Exit if canceled
					}

					// Only fetch if no email is found
					if (church.getEmail() == null || church.getEmail().isEmpty()) {
						String domain = extractDomain(church.getWebsite());
						if (domain != null && !domain.isEmpty()) {
							// Get emails from the domain asynchronously
							findEmailsByDomain(domain, emails -> {
								if (!emails.isEmpty()) {
									church.setEmail(String.join(", ", emails)); // Update email in church data
									System.out.println("Found emails for " + church.getName() + ": " + emails);
									javax.swing.SwingUtilities.invokeLater(() -> tableModel.fireTableDataChanged());
								} else {
									System.out.println("No emails found for " + church.getName());
								}
								// Update progress
								progress[0]++;
								progressBarDialog.updateProgress(progress[0], 0, churches.size());
							}, e -> {
								System.err
										.println("Error processing church " + church.getName() + ": " + e.getMessage());
								// Update progress even on error
								progress[0]++;
								progressBarDialog.updateProgress(progress[0], 0, churches.size());
							});
						}
					}
				}

				return null;
			}

			@Override
			protected void done() {
				progressBarDialog.dispose(); // Close the dialog when done

				if (onCompletion != null) {
					onCompletion.run(); // Call completion runnable
				}
			}
		};

		worker.execute();
		progressBarDialog.setVisible(true); // Display the progress dialog
	}

	private static String extractDomain(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}

		try {
			url = url.replaceFirst("^(https?://)?", "").replaceFirst("^(http?://)?", "");
			url = url.replaceFirst("^www\\.", "");
			return url.split("/")[0];
		} catch (Exception e) {
			System.err.println("Error extracting domain: " + e.getMessage());
			return null;
		}
	}

	private static void findEmailsByDomain(String domain, Consumer<List<String>> onEmailsFound,
			Consumer<Exception> onError) {
//		List<String> emails = new ArrayList<>();
		Config config = new Config();

		String url = String.format("%s?domain=%s&api_key=%s", config.getHunterUrl(), domain, config.getHunterApiKey());

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				System.err.println("Error fetching emails: " + e.getMessage());
				onError.accept(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (!response.isSuccessful()) {
					System.err.println("Failed to fetch data: " + response.message());
					onError.accept(new IOException("Unexpected code " + response));
					return;
				}

				// Use try-with-resources to ensure the response is closed properly
				try (Response res = response) { // Closing the response itself
					ResponseBody responseBody = res.body();
					if (responseBody != null) {
						String responseData = responseBody.string(); // Store response body
						System.out.println("Response from Hunter API: " + responseData); // Log the response

						List<String> emails = parseEmails(responseData); // Parse the emails from the response
						onEmailsFound.accept(emails); // Call the success handler with the found emails
					} else {
						System.err.println("Response body is null.");
						onEmailsFound.accept(new ArrayList<>()); // No emails found
					}
				} catch (IOException e) {
					System.err.println("Error reading response body: " + e.getMessage());
					onError.accept(e);
				} finally {
					// Close the response to ensure no connection is leaked
//			        if (res != null) {
//			            responseBody.close();  // Close the body if it was used
//			        }
					response.body().close(); // Ensure response is closed
				}
			}
		});
	}

	private static List<String> parseEmails(String jsonResponse) {
		List<String> emails = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// Parse the JSON response into a HunterData object
			HunterData hunterResponse = objectMapper.readValue(jsonResponse, HunterData.class);

			// Access the emails list from the Data object
			if (hunterResponse.getData() != null && hunterResponse.getData().getEmails() != null) {
				for (HunterData.Email email : hunterResponse.getData().getEmails()) {
					emails.add(email.getValue()); // Get the email value
				}
			}
		} catch (IOException e) {
			System.err.println("Error parsing JSON response: " + e.getMessage());
		}

		return emails; // Return the parsed list of emails
	}
}
