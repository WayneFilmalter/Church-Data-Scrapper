package churchDetailsFetcher.apiClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import churchDetailsFetcher.types.OverPassApiData;

import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import java.util.ArrayList;

public class LocationData {

    public static List<OverPassApiData.Element> getCitiesForState(String countryName, String regionName) {
        try {
            // Expanded Overpass API query to include towns, villages, and suburbs
            String query = String.format(
                    "[out:json];area[name=\"%s\"]->.country;"
                            + "area[name=\"%s\"]->.region;"
                            + "(node[place~\"city|town\"](area.region););out;",
                    countryName, regionName);

            String url = "http://overpass-api.de/api/interpreter?data=" + java.net.URLEncoder.encode(query, "UTF-8");

            // Make the HTTP GET request
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            conn.disconnect();

            // Parse the JSON response using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            OverPassApiData response = objectMapper.readValue(content.toString(), OverPassApiData.class);

            // Extract place names from the response
            List<OverPassApiData.Element> elements = response.getElements();
            String[] places = new String[elements.size()];

            for (int i = 0; i < elements.size(); i++) {
                OverPassApiData.Element element = elements.get(i);
                if (element.getTags() != null && element.getTags().getPlace() != null) {
                    places[i] = element.getTags().getName();
                }
            }

            return elements;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<OverPassApiData.Element>();
        }
    }
}