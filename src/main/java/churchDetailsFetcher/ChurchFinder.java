package churchDetailsFetcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import churchDetailsFetcher.apiClients.GooglePlacesAPI;
import churchDetailsFetcher.apiClients.LocationValidator;
import churchDetailsFetcher.errorrMessages.InvalidErrorMessage;
import churchDetailsFetcher.helpers.StringHelpers;
import churchDetailsFetcher.panels.LeftSidePanel;
import churchDetailsFetcher.panels.tabPanels.CityPanel;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;
import churchDetailsFetcher.types.DataTypes.GeoLongLat;
import churchDetailsFetcher.types.GooglePlacesApiData;
import churchDetailsFetcher.types.LocationCoordinates;

public class ChurchFinder implements ActionListener {
	private CityPanel cityPanel;
	private ChurchDataTableModel tableModel;
	private JLabel cityHeader;

	public ChurchFinder(CityPanel cityPanel, ChurchDataTableModel tableModel, JLabel cityHeader) {
		this.cityPanel = cityPanel;
		this.tableModel = tableModel;
		this.cityHeader = cityHeader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cityName = cityPanel.getCityFieldText();
		String regionName = cityPanel.getRegionFieldText();
		String countryName = cityPanel.getCountryFieldText();

		int range = cityPanel.getDistanceValue();

		tableModel.clearData();

		LocationCoordinates coordinates = LocationValidator.validateAndFetchCoordinates(cityName, regionName,
				countryName);

		if (cityName != null && regionName != null && countryName != null && coordinates.getCityIsValid()) {
			// Uncomment the API section to use Google Places API

			GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI();
			GeoLongLat cats = new GeoLongLat(coordinates.getLongitude(), coordinates.getLatitude());
			List<GooglePlacesApiData> churches = googlePlacesAPI.googlePlacesAPI(cats, countryName, regionName,
					cityName, range);

			cityHeader.setText("Results for " + StringHelpers.capitalizeString(cityName));

			for (GooglePlacesApiData church : churches) {
				ChurchTableData churchData = new ChurchTableData();
				churchData.setName(church.getName());
				churchData.setDenomination("");
				churchData.setAddress(StringHelpers.removeCommas(church.getAddress()));
				churchData.setPhoneNumber(church.getPhoneNumber());
				churchData.setWebsite(church.getWebsite());
				churchData.setEmail(church.getEmail());
				churchData.setRating(church.getRating());
				churchData.setUserRatingsTotal(church.getUserRatingsTotal());

				tableModel.addTableData(churchData);
			}

			setTableColumnVisibility();
			cityHeader.setText("Results for " + StringHelpers.capitalizeString(cityName));

		} else {
			InvalidErrorMessage.showInvalidCityPopup((JFrame) SwingUtilities.getWindowAncestor(cityPanel),
					cityPanel.getCityField());
			return;
		}
	}

	private void setTableColumnVisibility() {
		tableModel.setColumnVisible(ChurchDataTableModel.NAME_COLUMN, cityPanel.isNameCheckBoxSelected());
		tableModel.setColumnVisible(ChurchDataTableModel.DENOMINATION_COLUMN,
				cityPanel.isDenominationCheckBoxSelected());
		tableModel.setColumnVisible(ChurchDataTableModel.ADDRESS_COLUMN, cityPanel.isAddressCheckBoxSelected());
		tableModel.setColumnVisible(ChurchDataTableModel.PHONE_NUMBER_COLUMN,
				cityPanel.isPhoneNumberCheckBoxSelected());
		tableModel.setColumnVisible(ChurchDataTableModel.WEBSITE_COLUMN, cityPanel.isWebsiteCheckBoxSelected());
		tableModel.setColumnVisible(ChurchDataTableModel.EMAIL_COLUMN, cityPanel.isEmailCheckBoxSelected());
		tableModel.setColumnVisible(ChurchDataTableModel.RATING_COLUMN, cityPanel.isRatingCheckBoxSelected());
		tableModel.setColumnVisible(ChurchDataTableModel.RATINGS_COUNT_COLUMN,
				cityPanel.isUserRatingsTotalCheckBoxSelected());
	}

}