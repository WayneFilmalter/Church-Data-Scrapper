package churchScrapper;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.UnresolvedAddressException;

public class MonkAPI {
	public static void monkData() {

		System.out.print("Start Monk");
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
		
		try {
		    HttpRequest request = HttpRequest.newBuilder()
		            .uri(new URI("https://your-monkcms-api-url.com/endpoint"))  // Check the URL
		            .GET()
		            .build();
		    HttpClient client = HttpClient.newHttpClient();
		    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		    System.out.println(response.body());
		} catch (UnresolvedAddressException e) {
		    System.out.println("Invalid URL or address cannot be resolved: " + e.getMessage());
		} catch (ConnectException e) {
		    System.out.println("Failed to connect to the API: " + e.getMessage());
		} catch (Exception e) {
		    e.printStackTrace();
		}

		System.out.print("End Monk");

	}
}
