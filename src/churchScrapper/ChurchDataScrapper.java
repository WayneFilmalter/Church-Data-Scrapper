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
        this.leftPanel = leftPanel;  // Store the left panel
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
//        Object[] jsonResponse = apiClient.fetchChurchData(cityName, regionName, countryName);
        

        Object[] results = apiClient.fetchChurchData(cityName, regionName, countryName);

//        // Parse the fetched data
//        Object[][] churchData = apiClient.parseChurchData(jsonResponse);

        // Dynamically generate column headers based on selected checkboxes
        List<String> columnNames = new ArrayList<>();
        if (leftPanel.isSuburbCheckBoxSelected()) columnNames.add("Suburb");
        if (leftPanel.isNameCheckBoxSelected()) columnNames.add("Name");
        if (leftPanel.isEmailCheckBoxSelected()) columnNames.add("Email");
        if (leftPanel.isPhoneCheckBoxSelected()) columnNames.add("Phone");
        if (leftPanel.isStreetCheckBoxSelected()) columnNames.add("Street");
        if (leftPanel.isPlanningCenterCheckBoxSelected()) columnNames.add("Planning Center");

        // Add columns dynamically
        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }

//        // Create a sample row of data (this is where your web-scraping results will go)
//        Object[] fullDummyData = {
//            "Sample Suburb", 
//            "Sample Name", 
//            "sample@example.com", 
//            "123-456-7890", 
//            "123 Sample Street", 
//            "Yes"
//        };

        // Dynamically add row data based on selected columns
        List<Object> rowData = new ArrayList<>();
        for (String columnName : columnNames) {
            switch (columnName) {
                case "Suburb":
                    rowData.add(results[0]);
                    break;
                case "Name":
                    rowData.add(results[1]);
                    break;
                case "Email":
                    rowData.add(results[2]);
                    break;
                case "Phone":
                    rowData.add(results[3]);
                    break;
                case "Street":
                    rowData.add(results[4]);
                    break;
                case "Planning Center":
                    rowData.add(results[5]);
                    break;
            }
        }

        // Add the dynamically constructed row to the table model
        tableModel.addRow(rowData.toArray());
    }
}