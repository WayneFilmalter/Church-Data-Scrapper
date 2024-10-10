package churchScrapper;

import java.util.ArrayList;
import java.util.List;

public class ChurchScrapper {

	public List<String[]> searchChurches(String city, boolean email, boolean phone, boolean suburb, boolean street,
			boolean name, boolean planningCenter) {
		// Dummy data for demonstration purposes
		List<String[]> results = new ArrayList<>();

		// Add dummy data
		results.add(new String[] { "Suburb A", "Church A", email ? "emailA@example.com" : "",
				phone ? "(123) 456-7890" : "", street ? "123 Street A" : "", planningCenter ? "Yes" : "No" });
		results.add(new String[] { "Suburb B", "Church B", email ? "emailB@example.com" : "",
				phone ? "(987) 654-3210" : "", street ? "456 Street B" : "", planningCenter ? "No" : "Yes" });

		return results;
	}
}