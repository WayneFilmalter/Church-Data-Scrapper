package churchDetailsFetcher.types;

public class ChurchTableData {
	private String name;

	private String address;

	private String website;

	private String placeId;

	private String rating;

	private String phoneNumber;

	private String userRatingsTotal;

	private String email;

	private boolean hasWebsite;

	private boolean hasEmail;

	private boolean hasPhoneNumber;

	private boolean hasPCO;

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

	public boolean getHasWebsite() {
		return hasWebsite;
	}

	public void setHasWebsite(boolean hasWebsite) {
		this.hasWebsite = hasWebsite;
	}

	public boolean getHasEmail() {
		return hasEmail;
	}

	public void setHasEmail(boolean hasEmail) {
		this.hasEmail = hasEmail;
	}

	public boolean getHasPhoneNumber() {
		return hasPhoneNumber;
	}

	public void setHasPhoneNumber(boolean hasPhoneNumber) {
		this.hasPhoneNumber = hasPhoneNumber;
	}

	public boolean getHasPCO() {
		return hasPCO;
	}

	public void setHasPCO(boolean hasPCO) {
		this.hasPCO = hasPCO;
	}

	@Override
	public String toString() {
		return "Name: " + name + "\nAddress: " + address + "\nWebsite: " + website + "\nPhone: " + phoneNumber + "\n"
				+ placeId + "\n" + rating + "\n" + userRatingsTotal + "\n" + "\nEmail: " + email;
	}
}
