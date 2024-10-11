package churchScrapper.types;

public class OverPassTableData {
	private String name;
	private String denomination;
	private String address;
	private String website;
	private String phoneNumber;

	// Constructors
	public OverPassTableData(String name, String denomination, String address, String website, String phoneNumber) {
		this.name = name;
		this.denomination = denomination;
		this.address = address;
		this.website = website;
		this.phoneNumber = phoneNumber;

	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
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

	@Override
	public String toString() {
		return String.format("Name: %s, Denomination: %s, Address: %s, Website: %s, Phone: %s", name, denomination,
				address, website, phoneNumber);
	}
}
