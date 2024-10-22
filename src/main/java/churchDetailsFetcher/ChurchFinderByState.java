package churchDetailsFetcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.util.HashSet;
import java.util.Set;

import churchDetailsFetcher.apiClients.GooglePlacesAPI;
import churchDetailsFetcher.apiClients.LocationData;
import churchDetailsFetcher.apiClients.LocationValidator;
import churchDetailsFetcher.errorrMessages.InvalidErrorMessage;
import churchDetailsFetcher.helpers.StringHelpers;
import churchDetailsFetcher.panels.LeftSidePanel;
import churchDetailsFetcher.panels.tabPanels.CityPanel;
import churchDetailsFetcher.panels.tabPanels.StatePanel;
import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;
import churchDetailsFetcher.types.DataTypes.GeoLongLat;
import churchDetailsFetcher.types.GooglePlacesApiData;
import churchDetailsFetcher.types.LocationCoordinates;
import churchDetailsFetcher.types.OverPassApiData;

public class ChurchFinderByState implements ActionListener {
    private StatePanel statePanel;
    private ChurchDataTableModel tableModel;
    private JLabel cityHeader;

    public ChurchFinderByState(StatePanel statePanel, ChurchDataTableModel tableModel, JLabel cityHeader) {
        this.statePanel = statePanel;
        this.tableModel = tableModel;
        this.cityHeader = cityHeader;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("this ran");
        String regionName = statePanel.getStateFieldText();
        String countryName = statePanel.getCountryFieldText();

        tableModel.clearData();

        System.out.println("Start get all churches by region....");

        System.out.println(regionName + "  :  " + countryName);

        // for (String city : cities) {
        // System.out.println(countryName + regionName + city);
        // System.out
        // .println(city + " : " +
        // LocationValidator.validateAndFetchCoordinates(city.toString(), regionName,
        // countryName));

        // }

        if (regionName != null && countryName != null) {

            List<OverPassApiData.Element> cities = LocationData.getCitiesForState(countryName, regionName);
            // Uncomment the API section to use Google Places API

            System.out.println("Getting basic data for churches");

            List<GooglePlacesApiData> churches = SearchChurchesByState.getBaseChurchData(cities, regionName,
                    countryName,
                    new Config());

            System.out.println("getting detailed data for churches");

            List<GooglePlacesApiData> detailedChurchData = GooglePlacesAPI.getChurchDetails(new Config(), churches);

            Set<String> uniqueChurches = new HashSet<>();

            for (GooglePlacesApiData church : detailedChurchData) {
                String churchIdentifier = church.getName() + (church.getWebsite() == null ? "" : church.getWebsite());

                // Check if the church has already been added (duplicate name and website)
                if (!uniqueChurches.contains(churchIdentifier)) {
                    // Add the unique identifier to the set
                    uniqueChurches.add(churchIdentifier);

                    // Only proceed if this church is unique
                    ChurchTableData churchData = new ChurchTableData();
                    churchData.setName(church.getName());
                    churchData.setDenomination("");
                    churchData.setAddress(StringHelpers.removeCommas(church.getAddress()));
                    churchData.setPhoneNumber(church.getPhoneNumber());
                    churchData.setWebsite(church.getWebsite());
                    churchData.setEmail(church.getEmail());
                    churchData.setRating(church.getRating());
                    churchData.setUserRatingsTotal(church.getUserRatingsTotal());

                    // Add the unique church data to the table model
                    tableModel.addTableData(churchData);
                }
            }

            cityHeader.setText("Results for " + StringHelpers.capitalizeString(regionName));

        } else {
            System.out.println(regionName + " " + countryName);
            return;
        }

        System.out.println("Data retrived");

        System.out.println("End get all churches by region....");

    }

}
