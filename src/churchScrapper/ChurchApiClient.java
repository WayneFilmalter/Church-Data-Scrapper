package churchScrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import churchScrapper.types.OverPassApiData;
import churchScrapper.types.OverPassApiData.Element;

public class ChurchApiClient {

	private static final String API_URL = "https://example.com/api/churches"; // Replace with actual API URL

	ObjectMapper objectMapper = new ObjectMapper();
//	OverPassReturnType overPassReturnType = new OverPassReturnType();

	public void apples() {
		Config config = new Config();
		String latitude = "34.052235"; // Replace with your latitude
		String longitude = "-118.243683"; // Replace with your longitude
		String query = String.format(
				"%s?data=[out:json];node[\"amenity\"=\"place_of_worship\"][\"religion\"=\"christian\"](around:5000,%s,%s);out;",
				config.getOverPassAPIUrl(), latitude, longitude);

		StringBuilder response = new StringBuilder();

		try {
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// Deserialize the JSON response to a JsonNode
//			JsonNode jsonResponse = objectMapper.readValue(response.toString(), overPassReturnType.getClass());
			OverPassApiData results = objectMapper.readValue(response.toString(), OverPassApiData.class);
//			System.out.println(results.toString());
////			System.out.println(response.toString());
//			String jsonResult = objectMapper.writeValueAsString(results);
//	        System.out.println("Deserialized JSON Object: " + jsonResult);

//			OverPassReturnType over = new OverPassReturnType();
//
//			List<Element> trythis = results.getElements();
//			
//			Object[] mango = trythis.toArray();
//			
//			
//
//			System.out.print(mango.length);
			
			   for (OverPassApiData.Element element : results.getElements()) {
		            // Accessing tags
		            OverPassApiData.Element.Tags tags = element.getTags();

		            // Example: Adding a new field (e.g., a dummy website for each church)
		            String dummyWebsite = "http://example.com/" + tags.getName().replaceAll(" ", "_").toLowerCase();
		            tags.setWebsite(dummyWebsite);

		            // Example: Mutate the denomination to upper case
		            if (tags.getDenomination() != null) {
		                tags.setDenomination(tags.getDenomination().toUpperCase());
		            }

		            // Example: Log the modified element
		            System.out.println("Modified Element: " + objectMapper.writeValueAsString(element));
		        }

		} catch (JsonMappingException e) {
			System.err.println("Error mapping JSON to Church object: " + e.getMessage());
		} catch (JsonProcessingException e) {
			System.err.println("Error processing JSON: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
		}
//		catch (Exception e) {
//			e.printStackTrace();
//	    }

//		 // Deserialize the JSON response to a JsonNode
//        JsonNode jsonResponse = objectMapper.readTree(response.toString());
//        System.out.println(jsonResponse);

//		System.out.print(jsonResponse);

	};

//	public void apples() {
//		String latitude = "34.052235"; // Replace with your latitude
//		String longitude = "-118.243683"; // Replace with your longitude
//		String query = String.format(
//				"http://overpass-api.de/api/interpreter?data=[out:xml];node[\"amenity\"=\"place_of_worship\"][\"religion\"=\"christian\"](around:5000,%s,%s);out body;",
//				latitude, longitude);
//
//		try {
//			URL url = new URL(query);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String inputLine;
//			StringBuilder response = new StringBuilder();
//
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//
////			// Parse the JSON response
////			JSONObject jsonResponse = new JSONObject(response.toString());
////			JSONArray elements = jsonResponse.getJSONArray("elements");
////
////			for (int i = 0; i < elements.length(); i++) {
////				JSONObject church = elements.getJSONObject(i);
////				String name = church.optString("tags.name", "No name available");
////				String website = church.optString("tags.website", "No website available");
////
////				System.out.println("Church Name: " + name);
////				System.out.println("Website: " + website);
////				System.out.println("------------------------------");
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public Object[] fetchChurchData(String city, String state, String country) {
//		StringBuilder response = new StringBuilder();
//
//		try {
//			// Construct the query URL with parameters
//			String query = String.format("?city=%s&state=%s&country=%s", URLEncoder.encode(city, "UTF-8"),
//					URLEncoder.encode(state, "UTF-8"), URLEncoder.encode(country, "UTF-8"));
//
//			URL url = new URL(API_URL + query);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("GET");
//
//			// Check for HTTP response code
//			int responseCode = connection.getResponseCode();
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//				String inputLine;
//				while ((inputLine = in.readLine()) != null) {
//					response.append(inputLine);
//				}
//				in.close();
//			} else {
//				System.out.println("GET request failed: " + responseCode);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		System.out.print(response.toString());

		// return response.toString(); // Return the raw response

		apples();

		// Create a sample row of data (this is where your web-scraping results will go)
		Object[] fullDummyData = { "Sample Suburb", "Sample Name", "sample@example.com", "123-456-7890",
				"123 Sample Street", "Yes" };

		return fullDummyData;

	}

}