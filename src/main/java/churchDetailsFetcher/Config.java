package churchDetailsFetcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private Properties properties = new Properties();

	public Config() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
				return;
			}
			// Load properties file
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getOverPassAPIUrl() {
		return properties.getProperty("OVERPASS_API_URL");
	}

	public String getNominatimOpenStreetMapAPIUrl() {
		return properties.getProperty("NOMINATIM_OPEN_STREET_MAP_API");
	}

	public String getUserAgent() {
		return properties.getProperty("USER_AGENT");
	}

	public String getGooglePlacesApiKey() {
		return properties.getProperty("GOOGLE_PLACES_API_KEY");
	}

	public String getGooglePlacesUrl() {
		return properties.getProperty("GOOGLE_PLACES_API_URL");
	}

	public String getHunterUrl() {
		return properties.getProperty("HUNTER_URL");
	}

	public String getHunterApiKey() {
		return properties.getProperty("HUNTER_API_KEY");
	}

	public int getThreadPoolSize() {
		return Integer.valueOf(properties.getProperty("THREAD_POOL_SIZE"));
	}

}