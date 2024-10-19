package churchDetailsFetcher.apiClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import churchDetailsFetcher.Config;
import churchDetailsFetcher.helpers.StringHelpers;
import churchDetailsFetcher.types.LocationCoordinates;

public class LocationValidator {

	// Nominatim API URL for searching city by name, state, and country
	private static final String US_API_URL = "%ssearch?city=%s&state=%s&country=%s&format=json&limit=1";

	private static final String SA_API_URL = "%ssearch?city=%s&country=%s&format=json&limit=1";

	private static String getQuery(String baseUrl, String apiUrl, String cityName, String regionName,
			String countryName) {
		if (countryName.equalsIgnoreCase("South Africa")) {
			return String.format(apiUrl, baseUrl, StringHelpers.replaceSpacesWithPlus(cityName),
					StringHelpers.replaceSpacesWithPlus(countryName));
		} else {
			return String.format(apiUrl, baseUrl, StringHelpers.replaceSpacesWithPlus(cityName),
					StringHelpers.replaceSpacesWithPlus(regionName), StringHelpers.replaceSpacesWithPlus(countryName));
		}
	}

	public static LocationCoordinates validateAndFetchCoordinates(String cityName, String regionName,
			String countryName) {

		Config config = new Config();

		String API_URL;
		if (countryName.equalsIgnoreCase("South Africa")) {
			API_URL = (String) SA_API_URL;
		} else {
			API_URL = US_API_URL;
		}

		try {
			// Format the API URL
			String query = getQuery(config.getNominatimOpenStreetMapAPIUrl(), API_URL, cityName, regionName,
					countryName);

			System.out.print(query);

			@SuppressWarnings("deprecation")
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", config.getUserAgent());

			int responseCode = conn.getResponseCode();
			if (responseCode == 400) {
				System.out.println("Invalid request (400): Check the parameters.");
				return new LocationCoordinates(0, 0, false);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response.toString());

			if (rootNode.isArray() && rootNode.size() > 0) {
				JsonNode cityNode = rootNode.get(0);
				double lat = cityNode.get("lat").asDouble();
				double lon = cityNode.get("lon").asDouble();
				return new LocationCoordinates(lat, lon, true);
			} else {
				System.out.println("Location not found.");
				return new LocationCoordinates(0, 0, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new LocationCoordinates(0, 0, false);
		}
	}

}