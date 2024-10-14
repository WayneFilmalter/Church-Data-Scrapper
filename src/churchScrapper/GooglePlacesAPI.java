package churchScrapper;

import churchScrapper.types.GooglePlacesApiData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GooglePlacesAPI {
	public static void googlePlacesAPI() {

		System.out.print("Start places api");
//		try {
//			// Create HTTP client
//			HttpClient client = HttpClient.newHttpClient();
//
//			// Build the API request (replace with the actual API URL)
//			HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://your-monkcms-api-url.com/endpoint"))
//					.header("Content-Type", "application/json").GET().build();
//
//			// Send the request and get the response
//			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//			// Parse the JSON response
//			ObjectMapper objectMapper = new ObjectMapper();
//			JsonNode rootNode = objectMapper.readTree(response.body());
//
//			// Assuming emails are in a field like "email" in an array of church staff
//			JsonNode staffArray = rootNode.path("staff");
//			for (JsonNode staffMember : staffArray) {
//				String email = staffMember.path("email").asText();
//				String name = staffMember.path("name").asText();
//				System.out.println("Staff: " + name + ", Email: " + email);
//			}
//
//		} catch (Exception e) {
//			System.out.print(e);
//			e.printStackTrace();
//		}

//		try {
//		    HttpRequest request = HttpRequest.newBuilder()
//		            .uri(new URI("https://your-monkcms-api-url.com/endpoint"))  // Check the URL
//		            .GET()
//		            .build();
//		    HttpClient client = HttpClient.newHttpClient();
//		    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//		    System.out.println(response.body());

		Config config = new Config();

		String city = "bellville";

		String apiUrl = String.format("%stextsearch/json?query=churches+in+%s&key=%s", config.getGooglePlacesUrl(),
				city, config.getGooglePlacesApiKey());
		try {
			// Step 1: Make the API request
			URL url = new URL(apiUrl);
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

			// Step 2: Parse the response with ObjectMapper
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(content.toString());
			JsonNode resultsNode = rootNode.path("results");

			List<GooglePlacesApiData> churches = new ArrayList<>();
			if (resultsNode.isArray()) {
				for (JsonNode node : resultsNode) {
					GooglePlacesApiData church = objectMapper.treeToValue(node, GooglePlacesApiData.class);
					churches.add(church);
				}
			}

			// Step 3: Print out the details of each church
			for (GooglePlacesApiData church : churches) {
				System.out.println(church);
			}
		} catch (UnresolvedAddressException e) {
			System.out.println("Invalid URL or address cannot be resolved: " + e.getMessage());
		} catch (ConnectException e) {
			System.out.println("Failed to connect to the API: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.print("End places api");

	}
}
