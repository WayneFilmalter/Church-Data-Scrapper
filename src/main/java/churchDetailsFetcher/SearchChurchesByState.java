package churchDetailsFetcher;

import javax.swing.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import churchDetailsFetcher.types.DataTypes.GeoLongLat;
import churchDetailsFetcher.types.GooglePlacesApiData;
import churchDetailsFetcher.types.LocationCoordinates;
import churchDetailsFetcher.types.OverPassApiData;

import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import churchDetailsFetcher.apiClients.LocationData;
import churchDetailsFetcher.apiClients.LocationValidator;
import churchDetailsFetcher.types.GooglePlacesApiData;

public class SearchChurchesByState {

    // TODO: add boolean vals for selected functions
    public static void getAllChurchesByRegion(String regionName, String countryName,
            JFrame frame) {

        System.out.println("Start get all churches by region....");

        System.out.println(regionName + "  :  " + countryName);
        List<OverPassApiData.Element> cities = LocationData.getCitiesForState(frame, countryName, regionName);

        // for (String city : cities) {
        // System.out.println(countryName + regionName + city);
        // System.out
        // .println(city + " : " +
        // LocationValidator.validateAndFetchCoordinates(city.toString(), regionName,
        // countryName));

        // }

        getBaseChurchData(cities, regionName, countryName, new Config());

        System.out.println("End get all churches by region....");

    }

    public static List<GooglePlacesApiData> getBaseChurchData(List<OverPassApiData.Element> cities, String regionName,
            String countryName, Config config) {

        List<GooglePlacesApiData> combinedResults = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(config.getThreadPoolSize());

        System.out.println("Starting threads");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Future<List<GooglePlacesApiData>>> futures = new ArrayList<>();

            // Submit tasks to the executor
            for (OverPassApiData.Element city : cities) {
                futures.add(executor.submit(() -> fetchPlaceData(city, regionName, countryName, objectMapper, config)));
            }

            // Collect results and combine into a single list
            for (Future<List<GooglePlacesApiData>> future : futures) {
                try {
                    combinedResults.addAll(future.get()); // Combine all lists into one
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace(); // Handle exceptions during API calls
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown(); // Always shut down the executor
        }

        System.out.println("Ending threads");

        return combinedResults; // Return the combined list
    }

    private static List<GooglePlacesApiData> fetchPlaceData(OverPassApiData.Element city, String regionName,
            String countryName,
            ObjectMapper objectMapper, Config config) {

        System.out.println("starting for " + countryName + " : " + regionName + " : " + city.getLon());

        List<GooglePlacesApiData> results = new ArrayList<>();
        // String nextPageToken = null;

        try {
            // LocationCoordinates coordinates =
            // LocationValidator.validateAndFetchCoordinates(cityName, regionName,
            // countryName);

            String apiUrl = String.format("%stextsearch/json?query=churches&location=%f,%f&radius=%d&key=%s",
                    config.getGooglePlacesUrl(), city.getLat(),
                    city.getLon(), 5000,
                    config.getGooglePlacesApiKey());

            // // Make the HTTP GET request
            // HttpURLConnection conn = (HttpURLConnection) new
            // URL(apiUrl).openConnection();
            // conn.setRequestMethod("GET");

            // // Read the response
            // BufferedReader in = new BufferedReader(new
            // InputStreamReader(conn.getInputStream()));
            // StringBuilder content = new StringBuilder();
            // String inputLine;
            // while ((inputLine = in.readLine()) != null) {
            // content.append(inputLine);
            // }

            // in.close();
            // conn.disconnect();

            // Parse the JSON response using Jackson
            // response = objectMapper.readValue(content.toString(),
            // GooglePlacesApiData.class);

            @SuppressWarnings("deprecation")
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

            // ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(content.toString());
            JsonNode resultsNode = rootNode.path("results");

            if (resultsNode.isArray()) {
                for (JsonNode place : resultsNode) {
                    GooglePlacesApiData church = objectMapper.treeToValue(place, GooglePlacesApiData.class);
                    results.add(church);
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log errors for this specific city
        }

        System.out.println(results.toString());

        System.out.println("end for " + countryName + " : " + regionName + " : " + city.getTags().getName());

        // System.out.println(r.toString());
        return results;
    }

}
