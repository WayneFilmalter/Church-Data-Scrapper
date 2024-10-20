package churchDetailsFetcher.apiClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import churchDetailsFetcher.Config;
import churchDetailsFetcher.helpers.StringHelpers;
import churchDetailsFetcher.types.OverPassApiData;
import churchDetailsFetcher.types.TableData;

public class OverPassChurchFinder {

	ObjectMapper objectMapper = new ObjectMapper();

	public Object[][] apples(double latitude, double longitude, Integer range) {

		Config config = new Config();
		String query = String.format("%s?data=[out:json];node[\"amenity\"=\"place_of_worship\"](around:%s,%s,%s);out;",
				config.getOverPassAPIUrl(), range, String.valueOf(latitude), String.valueOf(longitude));

		StringBuilder response = new StringBuilder();
		List<TableData> churchDataList = new ArrayList<>();

		try {
			@SuppressWarnings("deprecation")
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
						? StringHelpers.capitalizeString(tags.getDenomination())
						: "";
				String address = tags.getAddrCity() != null && tags.getAddrState() != null
						? String.format("%s %s %s %s", tags.getAddrStreet(), tags.getAddrCity(), tags.getAddrState(),
								tags.getAddrPostcode())
						: "";
				String phoneNumber = tags.getPhone() != null ? tags.getPhone() : "";
				String website = tags.getWebsite() != null ? tags.getWebsite() : "";

				// Create a new OverPassTableData object
				TableData churchData = new TableData(name, denomination, address, phoneNumber, website);
				churchDataList.add(churchData);
			}

			// Convert List<OverPassTableData> to Object[][]
			Object[][] resultArray = new Object[churchDataList.size()][];
			for (int i = 0; i < churchDataList.size(); i++) {
				TableData churchData = churchDataList.get(i);
				resultArray[i] = new Object[] { churchData.getName(), churchData.getDenomination(),
						churchData.getAddress(), churchData.getPhoneNumber(), churchData.getWebsite() };
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

	public Object[][] fetchChurchData(double latitude, double longitude, Integer range) {

		return apples(latitude, longitude, range);

	}

}