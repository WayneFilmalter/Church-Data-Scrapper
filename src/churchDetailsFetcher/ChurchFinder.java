package churchDetailsFetcher;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import churchDetailsFetcher.errorrMessages.InvalidErrorMessage;
import churchDetailsFetcher.helpers.StringHelpers;
import churchDetailsFetcher.types.GooglePlacesApiData;
import churchDetailsFetcher.types.LocationCoordinates;
import churchDetailsFetcher.types.DataTypes.GeoLongLat;

public class ChurchFinder implements ActionListener {
	private LeftSidePanel leftPanel;
	private DefaultTableModel tableModel;
	private JLabel cityHeader;

	// Modify constructor to take LeftSidePanel and extract the necessary components
	public ChurchFinder(LeftSidePanel leftPanel, DefaultTableModel tableModel, JLabel cityHeader) {
		this.leftPanel = leftPanel;
		this.tableModel = tableModel;
		this.cityHeader = cityHeader;
	}

	private JDialog createLoadingDialog(JFrame parentFrame) {
		JDialog loadingDialog = new JDialog(parentFrame, "Loading", true);
		JLabel loadingLabel = new JLabel("Loading, please wait...", SwingConstants.CENTER);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true); // Show an indeterminate progress bar
		loadingDialog.setLayout(new BorderLayout());
		loadingDialog.add(loadingLabel, BorderLayout.NORTH);
		loadingDialog.add(progressBar, BorderLayout.CENTER);
		loadingDialog.setSize(300, 100);
		loadingDialog.setLocationRelativeTo(parentFrame);
		loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		return loadingDialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cityName = leftPanel.getCityFieldText();
		String regionName = leftPanel.getRegionFieldText();
		String countryName = leftPanel.getCountryFieldText();

		LocationCoordinates coordinates = LocationValidator.validateAndFetchCoordinates(cityName, regionName,
				countryName);

		if (cityName != null && regionName != null && countryName != null && coordinates.getCityIsValid()) {
			
			
			OverPassChurchFinder apiClient = new OverPassChurchFinder(); // free but few data
			GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI(); // free credits but billed when requests go over

			GeoLongLat cats = new GeoLongLat(coordinates.getLongitude(), coordinates.getLatitude());
			List<GooglePlacesApiData> churches = googlePlacesAPI.googlePlacesAPI(cats, countryName, regionName,
					cityName);

			// Clear previous columns and rows
			tableModel.setColumnCount(0);
			tableModel.setRowCount(0);

			// Dynamically generate column headers based on selected checkboxes
			List<String> columnNames = new ArrayList<>();
			if (leftPanel.isNameCheckBoxSelected())
				columnNames.add("Name");
			if (leftPanel.isDenominationCheckBoxSelected())
				columnNames.add("Denomination");
			if (leftPanel.isAddressCheckBoxSelected())
				columnNames.add("Address");
			if (leftPanel.isPhoneNumberCheckBoxSelected())
				columnNames.add("Phonenumber");
			if (leftPanel.isWebsiteCheckBoxSelected())
				columnNames.add("Website");
			if (leftPanel.isEmailCheckBoxSelected())
				columnNames.add("Email");
			if (leftPanel.isRatingCheckBoxSelected())
				columnNames.add("Rating");
			if (leftPanel.isUserRatingsTotalCheckBoxSelected())
				columnNames.add("Ratings Count");

			// Add columns dynamically
			for (String columnName : columnNames) {
				tableModel.addColumn(columnName);
			}

			cityHeader.setText("Results for " + StringHelpers.capitalizeString(cityName));

			// Add church data to the table
			for (GooglePlacesApiData church : churches) {
				List<Object> dynamicRowData = new ArrayList<>();

				// Use getters to populate row data
				for (String columnName : columnNames) {
					switch (columnName) {
					case "Name":
						dynamicRowData.add(church.getName() != null ? church.getName() : "N/A");
						break;
					case "Denomination":
						dynamicRowData.add("None");
						break;
					case "Address":
						dynamicRowData.add(church.getAddress() != null ? church.getAddress() : "N/A");
						break;
					case "Phonenumber":
						dynamicRowData.add(church.getPhoneNumber() != null ? church.getPhoneNumber() : "N/A");
						break;
					case "Website":
						dynamicRowData.add(church.getWebsite() != null ? church.getWebsite() : "N/A");
						break;
					case "Email":
						dynamicRowData.add(church.getEmail() != null ? church.getEmail() : "N/A");
						break;
					case "Rating":
						dynamicRowData.add(church.getRating() != null ? church.getRating() : "N/A");
						break;
					case "Ratings Count":
						dynamicRowData.add(church.getUserRatingsTotal() != null ? church.getUserRatingsTotal() : "N/A");
						break;

					}
				}

				tableModel.addRow(dynamicRowData.toArray());
			}
		} else {
			InvalidErrorMessage.showInvalidCityPopup((JFrame) SwingUtilities.getWindowAncestor(leftPanel),
					leftPanel.getCityField());
			return;
		}
	}

}