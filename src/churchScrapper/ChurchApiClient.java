package churchScrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import churchScrapper.types.OverPassApiData;
import churchScrapper.types.OverPassTableData;

import churchScrapper.helpers.StringHelpers;

public class ChurchApiClient {

	private static final String API_URL = "https://example.com/api/churches"; // Replace with actual API URL

	ObjectMapper objectMapper = new ObjectMapper();
//	OverPassReturnType overPassReturnType = new OverPassReturnType();

//	public List<OverPassTableData> apples() {
//		Config config = new Config();
//		String latitude = "34.052235"; // Replace with your latitude
//		String longitude = "-118.243683"; // Replace with your longitude
//		String query = String.format(
//				"%s?data=[out:json];node[\"amenity\"=\"place_of_worship\"][\"religion\"=\"christian\"](around:5000,%s,%s);out;",
//				config.getOverPassAPIUrl(), latitude, longitude);
//
//		StringBuilder response = new StringBuilder();
//		List<OverPassTableData> churchDataList = new ArrayList<>();
//
//		try {
//			URL url = new URL(query);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String inputLine;
//
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//			// Deserialize the JSON response to a JsonNode
////			JsonNode jsonResponse = objectMapper.readValue(response.toString(), overPassReturnType.getClass());
//			OverPassApiData results = objectMapper.readValue(response.toString(), OverPassApiData.class);
////			System.out.println(results.toString());
//////			System.out.println(response.toString());
////			String jsonResult = objectMapper.writeValueAsString(results);
////	        System.out.println("Deserialized JSON Object: " + jsonResult);
//
////			OverPassReturnType over = new OverPassReturnType();
////
////			List<Element> trythis = results.getElements();
////			
////			Object[] mango = trythis.toArray();
////			
////			
////
////			System.out.print(mango.length);
//
//			StringHelpers capitilize = new StringHelpers();
//
//			// Map and create the new shape for table data
//			for (OverPassApiData.Element element : results.getElements()) {
//				OverPassApiData.Element.Tags tags = element.getTags();
//				String name = tags.getName() != null ? tags.getName() : "No Name";
//				String denomination = tags.getDenomination() != null
//						? capitilize.capitalizeString(tags.getDenomination())
//						: "Unknown";
//				String address = String.format("%s, %s, %s, %s", tags.getAddrStreet(), tags.getAddrCity(),
//						tags.getAddrState(), tags.getAddrPostcode());
//				String website = tags.getWebsite() != null ? tags.getWebsite() : "No Website";
//				String phoneNumber = tags.getPhone() != null ? tags.getPhone() : "No Number";
//
//				// Create a new ChurchTableData object
//				OverPassTableData churchData = new OverPassTableData(name, denomination, address, website, phoneNumber);
//				churchDataList.add(churchData);
//			}
//
//			// Return the list of church data (you can change this based on your
//			// requirement)
//			for (OverPassTableData church : churchDataList) {
//				System.out.println("Table Data: " + objectMapper.writeValueAsString(church));
//			}
//			return churchDataList;
//
//		} catch (JsonMappingException e) {
//			System.err.println("Error mapping JSON to Church object: " + e.getMessage());
//		} catch (JsonProcessingException e) {
//			System.err.println("Error processing JSON: " + e.getMessage());
//		} catch (Exception e) {
//			System.err.println("An unexpected error occurred: " + e.getMessage());
//		}
////		catch (Exception e) {
////			e.printStackTrace();
////	    }
//		return churchDataList;
//
////		 // Deserialize the JSON response to a JsonNode
////        JsonNode jsonResponse = objectMapper.readTree(response.toString());
////        System.out.println(jsonResponse);
//
////		System.out.print(jsonResponse);
//
//	};

	public Object[][] apples() {
		Config config = new Config();
		String latitude = "34.052235"; // Replace with your latitude
		String longitude = "-118.243683"; // Replace with your longitude
		String query = String.format(
				"%s?data=[out:json];node[\"amenity\"=\"place_of_worship\"][\"religion\"=\"christian\"](around:5000,%s,%s);out;",
				config.getOverPassAPIUrl(), latitude, longitude);

		StringBuilder response = new StringBuilder();
		List<OverPassTableData> churchDataList = new ArrayList<>();

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

			// Deserialize the JSON response to OverPassApiData
			OverPassApiData results = objectMapper.readValue(response.toString(), OverPassApiData.class);

			StringHelpers capitilize = new StringHelpers();

			// Map and create the new shape for table data
			for (OverPassApiData.Element element : results.getElements()) {
				OverPassApiData.Element.Tags tags = element.getTags();
				String name = tags.getName() != null ? tags.getName() : "";
				String denomination = tags.getDenomination() != null
						? capitilize.capitalizeString(tags.getDenomination())
						: "";
				String address = tags.getAddrCity() != null && tags.getAddrState() != null
						? String.format("%s %s %s %s", tags.getAddrStreet(), tags.getAddrCity(), tags.getAddrState(),
								tags.getAddrPostcode())
						: "";
				String phoneNumber = tags.getPhone() != null ? tags.getPhone() : "";
				String website = tags.getWebsite() != null ? tags.getWebsite() : "";

				// Create a new OverPassTableData object
				OverPassTableData churchData = new OverPassTableData(name, denomination, address, phoneNumber, website);
				churchDataList.add(churchData);
			}

			// Convert List<OverPassTableData> to Object[][]
			Object[][] resultArray = new Object[churchDataList.size()][];
			for (int i = 0; i < churchDataList.size(); i++) {
				OverPassTableData churchData = churchDataList.get(i);
				resultArray[i] = new Object[] { churchData.getName(), churchData.getDenomination(),
						churchData.getAddress(), churchData.getPhoneNumber(), churchData.getWebsite() };
			}

			// Log the results
			for (Object[] church : resultArray) {
				System.out.println("Table Data: " + Arrays.toString(church));
			}

			return resultArray;

		} catch (JsonMappingException e) {
			System.err.println("Error mapping JSON to Church object: " + e.getMessage());
		} catch (JsonProcessingException e) {
			System.err.println("Error processing JSON: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
		}

		return new Object[0][]; // Return an empty 2D array in case of an error
	}

	public Object[][] fetchChurchData(String city, String state, String country) {
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
//		System.out.print(apples());
//
//		// return response.toString(); // Return the raw response
//
//		// Create a sample row of data (this is where your web-scraping results will go)
//		Object[] fullDummyData = { "Sample Suburb", "Sample Name", "sample@example.com", "123-456-7890",
//				"123 Sample Street", "Yes" };
//
//		return fullDummyData;

		return apples();

	}

}