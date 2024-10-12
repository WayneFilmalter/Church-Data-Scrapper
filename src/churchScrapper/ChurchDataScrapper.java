package churchScrapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import churchScrapper.errorrMessages.InvalidErrorMessage;
import churchScrapper.types.LocationCoordinates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChurchDataScrapper implements ActionListener {
	private LeftSidePanel leftPanel;
	private DefaultTableModel tableModel;
	private JLabel cityHeader;

	// Modify constructor to take LeftSidePanel and extract the necessary components
	public ChurchDataScrapper(LeftSidePanel leftPanel, DefaultTableModel tableModel, JLabel cityHeader) {
		this.leftPanel = leftPanel;
		this.tableModel = tableModel;
		this.cityHeader = cityHeader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cityName = leftPanel.getCityFieldText();
		String regionName = leftPanel.getRegionFieldText();
		String countryName = leftPanel.getCountryFieldText();

		LocationCoordinates coordinates = LocationValidator.validateAndFetchCoordinates(cityName, regionName,
				countryName);

		if (cityName != null && regionName != null && countryName != null && coordinates.getCityIsValid()) {

			ChurchApiClient apiClient = new ChurchApiClient();

			Object[][] results = apiClient.apples(coordinates.getLatitude(), coordinates.getLongitude(), 100000);

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
//		    if (leftPanel.isEmailCheckBoxSelected()) columnNames.add("Email");
			if (leftPanel.isPhoneNumberCheckBoxSelected())
				columnNames.add("Phonenumber");
			if (leftPanel.isWebsiteCheckBoxSelected())
				columnNames.add("Website");

			// Add columns dynamically
			for (String columnName : columnNames) {
				tableModel.addColumn(columnName);

			}

			cityHeader.setText("Results for " + cityName);

			for (Object[] rowData : results) { // Loop through each row in the results
				List<Object> dynamicRowData = new ArrayList<>();

				// Ensure we only access within the bounds of rowData
				for (String columnName : columnNames) {
					switch (columnName) {
					case "Name":
						if (rowData.length > 0) {
							dynamicRowData.add(rowData[0]);
						} else {
							dynamicRowData.add("N/A");
						}
						break;
					case "Denomination":
						if (rowData.length > 1) {
							dynamicRowData.add(rowData[1]);
						} else {
							dynamicRowData.add("N/A");
						}
						break;

					case "Address":
						if (rowData.length > 2) {
							dynamicRowData.add(rowData[2]);
						} else {
							dynamicRowData.add("N/A");
						}
						break;
					case "Phonenumber":
						if (rowData.length > 4) {
							dynamicRowData.add(rowData[4]);
						} else {
							dynamicRowData.add("N/A");
						}
						break;
					case "Website":
						if (rowData.length > 3) {
							dynamicRowData.add(rowData[3]);
						} else {
							dynamicRowData.add("N/A");
						}
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

		// Create an instance of ChurchApiClient

		// Fetch church data from the API
		// Ensure this returns Object[][]

		// Very primitive way of doing this but it works
		// Dynamically add row data based on selected columns

	}
}