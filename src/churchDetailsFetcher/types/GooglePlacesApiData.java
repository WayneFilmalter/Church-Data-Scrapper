package churchDetailsFetcher.types;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GooglePlacesApiData {
	private String name;

	@JsonProperty("formatted_address")
	private String address;

	private String website;

	@JsonProperty("place_id")
	private String placeId;

	@JsonProperty("rating")
	private String rating;

	@JsonProperty("formatted_phone_number")
	private String phoneNumber;

	@JsonProperty("user_ratings_total")
	private String userRatingsTotal;
	
	private String email;

	// Getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getUserRatingsTotal() {
		return userRatingsTotal;
	}

	public void setUserRatingsTotal(String userRatingsTotal) {
		this.userRatingsTotal = userRatingsTotal;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Name: " + name + "\nAddress: " + address + "\nWebsite: " + website + "\nPhone: " + phoneNumber + "\n"
				+ placeId + "\n" + rating + "\n" + userRatingsTotal + "\n" + "\nEmail: " +email;
	}
}