package churchScrapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
		this.leftPanel = leftPanel; // Store the left panel
		this.tableModel = tableModel;
		this.cityHeader = cityHeader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cityName = leftPanel.getCityFieldText(); // Get the city name entered by the user
		String regionName = leftPanel.getRegionFieldText(); // Assuming you have a getter for state
		String countryName = leftPanel.getCountryFieldText(); // Assuming you have a getter for country

		cityHeader.setText("Results for " + cityName);

		// Clear previous columns and rows
		tableModel.setColumnCount(0);
		tableModel.setRowCount(0);

		// Create an instance of ChurchApiClient
		ChurchApiClient apiClient = new ChurchApiClient();

		// Fetch church data from the API
		Object[][] results = apiClient.apples(); // Ensure this returns Object[][]

		// Dynamically generate column headers based on selected checkboxes
		List<String> columnNames = new ArrayList<>();
		if (leftPanel.isNameCheckBoxSelected())
			columnNames.add("Name");
		if (leftPanel.isDenominationCheckBoxSelected())
			columnNames.add("Denomination");
		if (leftPanel.isAddressCheckBoxSelected())
			columnNames.add("Address");
//	    if (leftPanel.isEmailCheckBoxSelected()) columnNames.add("Email");
		if (leftPanel.isPhoneNumberCheckBoxSelected())
			columnNames.add("Phonenumber");
		if (leftPanel.isWebsiteCheckBoxSelected())
			columnNames.add("Website");

		// Add columns dynamically
		for (String columnName : columnNames) {
			tableModel.addColumn(columnName);

		}

		// Dynamically add row data based on selected columns
		for (Object[] rowData : results) { // Loop through each row in the results
			List<Object> dynamicRowData = new ArrayList<>();

			// Ensure we only access within the bounds of rowData
			for (String columnName : columnNames) {
				switch (columnName) {
				case "Name":
					if (rowData.length > 0) {
						dynamicRowData.add(rowData[0]); // Ensure index 0 is within bounds
					} else {
						dynamicRowData.add("N/A"); // Default value if out of bounds
					}
					break;
				case "Denomination":
					if (rowData.length > 1) {
						dynamicRowData.add(rowData[1]); // Ensure index 2 is within bounds
					} else {
						dynamicRowData.add("N/A"); // Default value if out of bounds
					}
					break;
					
				case "Address":
					if (rowData.length > 2) {
						dynamicRowData.add(rowData[2]); // Ensure index 3 is within bounds
					} else {
						dynamicRowData.add("N/A"); // Default value if out of bounds
					}
					break;
				case "Phonenumber":
					if (rowData.length > 3) {
						dynamicRowData.add(rowData[3]); // Ensure index 4 is within bounds
					} else {
						dynamicRowData.add("N/A"); // Default value if out of bounds
					}
					break;
				case "Website":
					if (rowData.length > 4) {
						dynamicRowData.add(rowData[4]); // Ensure index 5 is within bounds
					} else {
						dynamicRowData.add("N/A"); // Default value if out of bounds
					}
					break;
				}
			}

			// Add the dynamically constructed row to the table model
			tableModel.addRow(dynamicRowData.toArray());
		}
	}
}