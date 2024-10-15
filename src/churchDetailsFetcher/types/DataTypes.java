package churchDetailsFetcher.types;

public class DataTypes {

	public static class GeoLongLat {

		private double longitude;
		private double latitude;

		public GeoLongLat(double longitude, double latitude) {
			this.longitude = longitude;
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		@Override
		public String toString() {
			return "GeoLongLat{longitude=" + longitude + ", latitude=" + latitude + "}";
		}
	}

	public static class NamePhoneNumber {

		private String name;
		private String phoneNumber;

		public NamePhoneNumber(String name, String phoneNumber) {
			this.name = name;
			this.phoneNumber = phoneNumber;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@Override
		public String toString() {
			return "Contacts{name=" + name + ", phoneNumber=" + phoneNumber + "}";
		}
	}
	
	public static class NameEmail {

		private String name;
		private String email;

		public NameEmail(String name, String email) {
			this.name = name;
			this.email = email;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@Override
		public String toString() {
			return "Contacts{name=" + name + ", email=" + email + "}";
		}
	}

}