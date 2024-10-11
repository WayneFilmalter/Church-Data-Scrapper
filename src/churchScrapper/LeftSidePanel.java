package churchScrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftSidePanel extends JPanel {

	private static final long serialVersionUID = 271870163847946239L;

	private JComboBox<String> countryComboBox, regionComboBox;
	private JTextField cityField;
	private JCheckBox nameCheckBox, denominationCheckBox, addressCheckBox, phoneCheckBox, websiteCheckBox;
	private JButton searchButton;

	public LeftSidePanel() {
		setLayout(new GridBagLayout());
		setBackground(new Color(224, 255, 255)); // Light blue/cyan background

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.NORTHWEST;

		// Input components
		countryComboBox = new JComboBox<>(new String[] { "Select Country", "USA", "South Africa" });
		regionComboBox = new JComboBox<>(new String[] { null });
		regionComboBox.setVisible(false);

		cityField = new JTextField(15);
		cityField.setEnabled(false); // Initially disable the city field

		nameCheckBox = new JCheckBox("Name", true);
//		emailCheckBox = new JCheckBox("Email", true);
		denominationCheckBox = new JCheckBox("Denomination", true);
		phoneCheckBox = new JCheckBox("Phonenumber", true);
		addressCheckBox = new JCheckBox("Address", true);
		websiteCheckBox = new JCheckBox("Website", true);

		searchButton = new JButton("Search");
		searchButton.setPreferredSize(new Dimension(150, 40));
		searchButton.setBackground(new Color(0, 204, 153));

		// Add components
		add(new JLabel("Country: "), gbc);
		add(countryComboBox, gbc);
		add(new JLabel("State: "), gbc);
		add(regionComboBox, gbc);
		add(new JLabel("City: "), gbc);
		add(cityField, gbc);
//		add(emailCheckBox, gbc);

		add(nameCheckBox, gbc);
		add(denominationCheckBox, gbc);
		add(addressCheckBox, gbc);
		add(phoneCheckBox, gbc);
		add(websiteCheckBox, gbc);

		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(searchButton, gbc);

		// Add the ActionListener for country selection
		countryComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedCountry = (String) countryComboBox.getSelectedItem();
				regionComboBox.removeAllItems(); // Clear previous items
				regionComboBox.setVisible(false); // Hide the region dropdown initially
				cityField.setEnabled(false); // Disable the city field initially

				// Populate based on the selected country
				if ("USA".equals(selectedCountry)) {
					regionComboBox.setVisible(true);
					for (String state : CountryData.usaStates) {
						regionComboBox.addItem(state);
					}
				} else if ("South Africa".equals(selectedCountry)) {
					regionComboBox.setVisible(true);
					for (String province : CountryData.saProvinces) {
						regionComboBox.addItem(province);
					}
				} else
					regionComboBox.setVisible(false);
			}
		});

		// Add ActionListener for state selection
		regionComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Enable city input only when a state/province is selected
				if (regionComboBox.getSelectedItem() != null) {
					cityField.setEnabled(true);
				} else {
					cityField.setEnabled(false);
				}
			}
		});

	}

	// Getters for the input fields and search button
	public String getCountryFieldText() {
		return countryComboBox.getSelectedItem().toString();
	}

	public String getRegionFieldText() {
//		return regionComboBox.getText();
		return regionComboBox.getSelectedItem().toString();
	}

	public String getCityFieldText() {
		return cityField.getText();
	}

	public JButton getSearchButton() {
		return searchButton;
	}

//	public boolean isEmailCheckBoxSelected() {
//		return emailCheckBox.isSelected();
//	}

	public boolean isPhoneNumberCheckBoxSelected() {
		return phoneCheckBox.isSelected();
	}

	public boolean isDenominationCheckBoxSelected() {
		return denominationCheckBox.isSelected();
	}

	public boolean isAddressCheckBoxSelected() {
		return addressCheckBox.isSelected();
	}

	public boolean isNameCheckBoxSelected() {
		return nameCheckBox.isSelected();
	}

	public boolean isWebsiteCheckBoxSelected() {
		return websiteCheckBox.isSelected();
	}

	// Other getters as needed for form fields...
}