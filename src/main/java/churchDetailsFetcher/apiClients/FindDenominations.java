package churchDetailsFetcher.apiClients;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import com.fasterxml.jackson.databind.ObjectMapper;

import churchDetailsFetcher.Config;
import churchDetailsFetcher.dialog.ProgressBarDialog;
import churchDetailsFetcher.helpers.StringHelpers;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;
import churchDetailsFetcher.types.OverPassApiData;

public class FindDenominations {

	public static void scrapeDenominations(ChurchDataTableModel tableModel, Component parentComponent,
			Runnable onCompletion) {
		List<ChurchTableData> churches = tableModel.getTableData(); // Assuming you have a method to get church data

		List<String> denominations = Arrays.asList("Baptist", "Catholic", "Presbyterian", "Methodist", "Lutheran",
				"Pentecostal", "NG", "Anglican", "Dutch Reformed");

		// Create and display the custom ProgressBarDialog
		ProgressBarDialog progressBarDialog = new ProgressBarDialog((JFrame) parentComponent, "Finding Denominations");
		progressBarDialog.setProgressMax(churches.size());

		// Use an ExecutorService to handle the scraping in parallel
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<Void>> futures = new ArrayList<>();
		AtomicInteger progress = new AtomicInteger(0); // For thread-safe progress updates
		AtomicInteger found = new AtomicInteger(0); // For thread-safe count of found denominations

		for (ChurchTableData church : churches) {
			// Submit each scraping task to the executor service
			futures.add(executorService.submit(() -> {
				if (progressBarDialog.isCancelled()) {
					return null; // Exit if cancelled
				}

				String churchName = church.getName();
				if (churchName != null && !churchName.isEmpty()) {
					String denomination = getDenominationByChurchName(churchName);
					if (denomination != null && !denomination.isEmpty()) {
						church.setDenomination(StringHelpers.capCaseString(denomination)); // Update the church data
						found.incrementAndGet(); // Increment found count
						System.out.println("Found denomination for " + church.getName() + ": " + denomination);
					} else {
						for (String denominationName : denominations) {
							if (churchName.toLowerCase().contains(denominationName.toLowerCase())) {
								if (denominationName.equalsIgnoreCase("Dutch Reformed")
										|| denominationName.equalsIgnoreCase("Nederduitse Gereformeerde")) {
									church.setDenomination("NG");
									System.out.println("Found denomination in name for " + church.getName() + ": NG");
								} else {
									church.setDenomination(denominationName);
									System.out.println("Found denomination in name for " + church.getName() + ": "
											+ denominationName);
								}
							}
						}
						System.out.println("No denomination found for " + church.getName());
					}
				} else {
					System.out.println("No church name provided for " + church.getName());
				}

				// Update progress bar
				int currentProgress = progress.incrementAndGet();
				progressBarDialog.updateProgress(currentProgress, found.get(), churches.size());

				return null; // Return null as required by Future<Void>
			}));
		}

		// Use a separate thread to monitor the completion of all tasks
		new Thread(() -> {
			try {
				for (Future<Void> future : futures) {
					future.get(); // Wait for each task to complete
				}
			} catch (Exception e) {
				System.err.println("Error while waiting for tasks to complete: " + e.getMessage());
			} finally {
				executorService.shutdown(); // Shutdown the executor service
				progressBarDialog.dispose(); // Close the progress dialog when scraping is complete

				if (!progressBarDialog.isCancelled()) {
					// Show notification or handle completion
					if (onCompletion != null) {
						onCompletion.run(); // Trigger the PCO check
					}
					System.out.println("Denomination scraping completed.");
				}
			}
		}).start();

		progressBarDialog.setVisible(true); // Display the progress bar
	}

	// Subfunction to get the denomination by church name from OpenStreetMap
	private static String getDenominationByChurchName(String churchName) {

		ObjectMapper objectMapper = new ObjectMapper();
		StringBuilder response = new StringBuilder();
		Config config = new Config();

		try {
			// Encode the church name
			String encodedChurchName = URLEncoder.encode(churchName, StandardCharsets.UTF_8.toString());

			// Construct the Overpass API query
			String query = String.format("%s?data=[out:json];node[\"amenity\"=\"place_of_worship\"][name=\"%s\"];out;",
					config.getOverPassAPIUrl(), encodedChurchName);

			// Use HttpURLConnection to query the Overpass API
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// Read the response
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Deserialize the JSON response
			OverPassApiData results = objectMapper.readValue(response.toString(), OverPassApiData.class);

			// Check if the response contains elements
			if (results.getElements() != null && !results.getElements().isEmpty()) {
				return results.getElements().get(0).getTags().getDenomination();
			}

		} catch (Exception e) {
			System.err.println("Error querying denomination for " + churchName + ": " + e.getMessage());
		}

		return null; // Return null if no denomination is found
	}
}