package churchDetailsFetcher.panels.tabPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import churchDetailsFetcher.data.CountryData;

public class StatePanel extends JPanel {

    private static final long serialVersionUID = 271870163847946239L;

    private JComboBox<String> countryComboBox;
    private JComboBox<String> stateComboBox;
    private JCheckBox denominationCheckBox, webScrapingCheckBox, hunterEmailsCheckBox, pcoCheckBox;
    private JButton searchButton;

    public StatePanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(224, 255, 255)); // Light blue/cyan background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Input components
        countryComboBox = new JComboBox<>(new String[] { "Select Country", "USA", "South Africa" });
        stateComboBox = new JComboBox<>(new String[] { null });
        stateComboBox.setVisible(false); // Initially hide the state dropdown

        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(150, 40));
        searchButton.setBackground(new Color(0, 204, 153));

        // Checkboxes for the data options (using the specified checkboxes)
        denominationCheckBox = new JCheckBox("Denomination");
        webScrapingCheckBox = new JCheckBox("Web Scraping");
        hunterEmailsCheckBox = new JCheckBox("Hunter Emails");
        pcoCheckBox = new JCheckBox("PCO Check");

        // Add components
        add(new JLabel("Country: "), gbc);
        add(countryComboBox, gbc);
        add(new JLabel("State: "), gbc);
        add(stateComboBox, gbc);

        // Add checkboxes
        add(denominationCheckBox, gbc);
        add(webScrapingCheckBox, gbc);
        add(hunterEmailsCheckBox, gbc);
        add(pcoCheckBox, gbc);

        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        add(searchButton, gbc);

        // Add ActionListener for country selection
        countryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCountry = (String) countryComboBox.getSelectedItem();
                stateComboBox.removeAllItems(); // Clear previous items
                stateComboBox.setVisible(false); // Hide the state dropdown initially

                // Populate based on the selected country
                if ("USA".equals(selectedCountry)) {
                    stateComboBox.setVisible(true);
                    for (String state : CountryData.usaStates) {
                        stateComboBox.addItem(state);
                    }
                } else if ("South Africa".equals(selectedCountry)) {
                    stateComboBox.setVisible(true);
                    for (String province : CountryData.saProvinces) {
                        stateComboBox.addItem(province);
                    }
                } else {
                    stateComboBox.setVisible(false);
                }
            }
        });
    }

    // Getters for the input fields and search button
    public String getCountryFieldText() {
        return countryComboBox.getSelectedItem().toString();
    }

    public String getStateFieldText() {
        return stateComboBox.getSelectedItem() != null ? stateComboBox.getSelectedItem().toString() : null;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public boolean isDenominationCheckBoxSelected() {
        return denominationCheckBox.isSelected();
    }

    public boolean isWebScrapingCheckBoxSelected() {
        return webScrapingCheckBox.isSelected();
    }

    public boolean isHunterEmailsCheckBoxSelected() {
        return hunterEmailsCheckBox.isSelected();
    }

    public boolean isPcoCheckBoxSelected() {
        return pcoCheckBox.isSelected();
    }
}