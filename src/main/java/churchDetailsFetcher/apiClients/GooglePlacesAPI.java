package churchDetailsFetcher.apiClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import churchDetailsFetcher.Config;
import churchDetailsFetcher.helpers.StringHelpers;
import churchDetailsFetcher.types.DataTypes.GeoLongLat;
import churchDetailsFetcher.types.GooglePlacesApiData;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GooglePlacesAPI {

	public List<GooglePlacesApiData> googlePlacesAPI(GeoLongLat coordinates, String countryName, String regionName,
			String cityName, int range) {
		System.out.println("Getting data from " + cityName);

		Config config = new Config();

		List<GooglePlacesApiData> churches = getChurchesInRange(config, coordinates, range);

		List<GooglePlacesApiData> churchDetails = getChurchDetails(config, churches);

		List<GooglePlacesApiData> filteredChurches = churchDetails.stream().filter(church -> church.getWebsite() != null
				&& !church.getWebsite().isEmpty() && church.getName() != null && !church.getName().isEmpty()).toList();

		System.out.println("End places api");

		return filteredChurches;
	}

	public static List<GooglePlacesApiData> getChurchDetails(Config config, List<GooglePlacesApiData> churches) {
		List<GooglePlacesApiData> results = new ArrayList<>();

		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService executorService = Executors.newFixedThreadPool(threads); // Use an appropriate thread count
		List<Future<GooglePlacesApiData>> futures = new ArrayList<>();

		for (GooglePlacesApiData church : churches) {
			// Check if the church already has a phone number or a website
			if (church.getPhoneNumber() == null || church.getPhoneNumber().isEmpty() ||
					church.getWebsite() == null || church.getWebsite().isEmpty()) {

				// For each church, submit a Callable to the ExecutorService
				Future<GooglePlacesApiData> future = executorService.submit(new Callable<GooglePlacesApiData>() {
					@Override
					public GooglePlacesApiData call() throws Exception {
						String placeId = church.getPlaceId();
						String detailsUrl = String.format("%sdetails/json?place_id=%s&key=%s",
								config.getGooglePlacesUrl(),
								placeId, config.getGooglePlacesApiKey());

						URL url = new URL(detailsUrl);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");

						BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						StringBuilder content = new StringBuilder();
						String inputLine;
						while ((inputLine = in.readLine()) != null) {
							content.append(inputLine);
						}
						in.close();
						conn.disconnect();

						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode rootNode = objectMapper.readTree(content.toString());
						JsonNode resultNode = rootNode.path("result");

						System.out.println("Getting data for: " + resultNode.path("name").asText());

						GooglePlacesApiData churchDetails = new GooglePlacesApiData();
						churchDetails.setPlaceId(placeId);
						churchDetails.setName(StringHelpers.removeCommas(resultNode.path("name").asText()));
						churchDetails
								.setAddress(StringHelpers.removeCommas(resultNode.path("formatted_address").asText()));
						churchDetails.setPhoneNumber(resultNode.path("formatted_phone_number").asText());
						churchDetails.setWebsite(resultNode.path("website").asText());
						churchDetails.setEmail(resultNode.path("email").asText());
						churchDetails.setRating(resultNode.path("rating").asText());
						churchDetails.setUserRatingsTotal(resultNode.path("user_ratings_total").asText());

						return churchDetails;
					}
				});

				// Add the future to the list
				futures.add(future);
			} else {
				// If phone number or website already exists, add the existing church to results
				results.add(church);
			}
		}

		// Wait for all the tasks to complete and gather results
		for (Future<GooglePlacesApiData> future : futures) {
			try {
				results.add(future.get()); // This will block until the result is available
			} catch (Exception e) {
				e.printStackTrace(); // Handle any exceptions
			}
		}

		// Shut down the ExecutorService
		executorService.shutdown();

		return results;
	}

	public static GeoLongLat getCoordinates(Config config, String countryName, String regionName, String cityName) {
		GeoLongLat coordinates = new GeoLongLat(0, 0);

		try {
			String address = String.format("%s,%s,%s", "south+africa", "western+cape", "bellville");
			String geocodeUrl = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
					java.net.URLEncoder.encode(address, "UTF-8"), config.getGooglePlacesApiKey());

			@SuppressWarnings("deprecation")
			URL url = new URL(geocodeUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder content = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			conn.disconnect();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(content.toString());
			JsonNode resultsNode = rootNode.path("results");

			System.out.println("Geocoding API Response: " + content.toString());

			if (resultsNode.isArray() && resultsNode.size() > 0) {
				JsonNode locationNode = resultsNode.get(0).path("geometry").path("location");
				coordinates.setLatitude(locationNode.path("lat").asDouble());
				coordinates.setLongitude(locationNode.path("lng").asDouble());
			} else {
				System.out.println("No results found for the given address: " + address);
			}

			System.out.println("Coordinates: " + coordinates.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return coordinates;
	}

	public static List<GooglePlacesApiData> getChurchesInRange(Config config, GeoLongLat coordinates, int range) {
		String apiUrl = String.format("%stextsearch/json?query=churches&location=%f,%f&radius=%d&key=%s",
				config.getGooglePlacesUrl(), coordinates.getLatitude(),
				coordinates.getLongitude(), range,
				config.getGooglePlacesApiKey());

		// Print the final API URL for debugging
		System.out.println("Request URL: " + apiUrl);

		List<GooglePlacesApiData> results = new ArrayList<>();
		String nextPageToken = null;

		try {
			do {
				String pagedUrl = nextPageToken != null ? apiUrl + "&pagetoken=" + nextPageToken : apiUrl;

				@SuppressWarnings("deprecation")
				URL url = new URL(pagedUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder content = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				in.close();
				conn.disconnect();

				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(content.toString());
				JsonNode resultsNode = rootNode.path("results");

				if (resultsNode.isArray()) {
					for (JsonNode place : resultsNode) {
						GooglePlacesApiData church = objectMapper.treeToValue(place, GooglePlacesApiData.class);
						results.add(church);
					}
				}

				nextPageToken = rootNode.path("next_page_token").asText(null);

				// If there is a next_page_token, wait for 2 seconds
				if (nextPageToken != null && !nextPageToken.isEmpty()) {
					System.out.println("Waiting for next_page_token...");
					Thread.sleep(2000); // Ensure enough delay before the next request
				} else {
					nextPageToken = null; // Stop the loop if no valid token
				}

			} while (nextPageToken != null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}
}