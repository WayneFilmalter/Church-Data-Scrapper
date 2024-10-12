package churchScrapper.types;

public class LocationCoordinates {
	private double latitude;
	private double longitude;
	private boolean cityIsValid;

	public LocationCoordinates(double latitude, double longitude, boolean cityIsValid) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.cityIsValid = cityIsValid;
	}

	@Override
	public String toString() {
		return "Latitude: " + latitude + ", Longitude: " + longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public boolean getCityIsValid() {
		return cityIsValid;
	}

	public void setCityIsValid(boolean cityIsValid) {
		this.cityIsValid = cityIsValid;
	}
}