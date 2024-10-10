package churchScrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.event.ActionEvent;

public class ChurchScrapperApp {

	public static void main(String[] args) {

		// Set a global font for all UI components
		Font globalFont = new Font("Arial", Font.PLAIN, 18); // 18-point font size
		UIManager.put("Label.font", globalFont);
		UIManager.put("Button.font", globalFont);
		UIManager.put("TextField.font", globalFont);
		UIManager.put("CheckBox.font", globalFont);
		UIManager.put("Table.font", globalFont);
		UIManager.put("TableHeader.font", globalFont);

		// Create the main frame
		JFrame frame = new JFrame("Church Finder");
		frame.setSize(1400, 900); // Increase the window size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a split pane for dividing the window
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(300); // Left side 300px, right side takes the rest
		frame.add(splitPane);

		// LEFT SIDE: Input Panel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout()); // For aligning components vertically
		inputPanel.setBackground(new Color(224, 255, 255)); // Light blue/cyan background
		splitPane.setLeftComponent(inputPanel);

		// Create the input components
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.NORTHWEST;

		// Country dropdown
		JLabel countryLabel = new JLabel("Country: ");
		JComboBox<String> countryComboBox = new JComboBox<>(new String[] { "Select Country", "USA", "South Africa" });
		countryComboBox.setFont(globalFont);

		// State/Province/County dropdown
		JLabel regionLabel = new JLabel("State: ");
		JComboBox<String> regionComboBox = new JComboBox<>(new String[] { null });
		regionComboBox.setFont(globalFont);

		// Add an ActionListener to populate the region dropdown based on selected
		// country
		countryComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedCountry = (String) countryComboBox.getSelectedItem();
				regionComboBox.removeAllItems(); // Clear previous items

				// Example population based on selected country
				if ("USA".equals(selectedCountry)) {

					for (String state : CountryData.usaStates) {
						regionComboBox.addItem(state);
					}

				} else if ("South Africa".equals(selectedCountry)) {
					for (String province : CountryData.saProvinces) {
						regionComboBox.addItem(province);
					}
				} else {
//                    regionComboBox.addItem("Select State");
				}
			}
		});

		JLabel cityLabel = new JLabel("City: ");
		JTextField cityField = new JTextField(15); // Input for city
		JCheckBox emailCheckBox = new JCheckBox("Email", true);
		JCheckBox phoneCheckBox = new JCheckBox("Phone Number", true);
		JCheckBox suburbCheckBox = new JCheckBox("Suburb", true);
		JCheckBox streetCheckBox = new JCheckBox("Street", true);
		JCheckBox nameCheckBox = new JCheckBox("Name", true);
		JCheckBox planningCenterCheckBox = new JCheckBox("Only Planning Center", true);

//        // Set font size for checkboxes
//        emailCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
//        phoneCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
//        suburbCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
//        streetCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
//        nameCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));
//        planningCenterCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));

		JButton searchButton = new JButton("Search");
		searchButton.setPreferredSize(new Dimension(150, 40));
		searchButton.setBackground(new Color(0, 204, 153)); // Light green/teal
		searchButton.setForeground(Color.BLACK);

		// Add the components to the left panel
		inputPanel.add(countryLabel, gbc);
		inputPanel.add(countryComboBox, gbc);
		inputPanel.add(regionLabel, gbc);
		inputPanel.add(regionComboBox, gbc);
		inputPanel.add(cityLabel, gbc);
		inputPanel.add(cityField, gbc);
		inputPanel.add(emailCheckBox, gbc);
		inputPanel.add(phoneCheckBox, gbc);
		inputPanel.add(suburbCheckBox, gbc);
		inputPanel.add(streetCheckBox, gbc);
		inputPanel.add(nameCheckBox, gbc);
		inputPanel.add(planningCenterCheckBox, gbc);

		inputPanel.setFont(new Font("Arial", Font.PLAIN, 18));

		gbc.weighty = 1.0; // Push the button to the bottom
		gbc.anchor = GridBagConstraints.SOUTH;
		inputPanel.add(searchButton, gbc);

		// RIGHT SIDE: Results Panel
		JPanel resultsPanel = new JPanel(new BorderLayout());
		splitPane.setRightComponent(resultsPanel);
		// resultsPanel.setFont("arial",20);

		// City header label (shows the city name)
		JLabel cityHeader = new JLabel("No Results", SwingConstants.CENTER);
		cityHeader.setFont(new Font("Arial", Font.BOLD, 20));
		resultsPanel.add(cityHeader, BorderLayout.NORTH);

		// Table for displaying results
		String[] columnNames = { "Suburb", "Name", "Email", "Phone", "Street", "Planning Center" };
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		JTable resultsTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(resultsTable);
		resultsPanel.add(scrollPane, BorderLayout.CENTER);
		resultsPanel.setFont(new Font("Arial", Font.PLAIN, 18));

		// Buttons at the bottom
		JPanel buttonPanel = new JPanel();
		JButton exportButton = new JButton("Export to CSV");
		JButton saveButton = new JButton("Save");
		JButton copyEmailsButton = new JButton("Copy Emails");

		// Set preferred sizes for buttons
		exportButton.setPreferredSize(new Dimension(150, 40));
		saveButton.setPreferredSize(new Dimension(150, 40));
		copyEmailsButton.setPreferredSize(new Dimension(150, 40));

		buttonPanel.add(exportButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(copyEmailsButton);
		resultsPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Action Listener for the Search Button (you can update it with your scraping
		// logic)
		ChurchScrapperFunctions actions = new ChurchScrapperFunctions(cityField, emailCheckBox, phoneCheckBox,
				suburbCheckBox, streetCheckBox, nameCheckBox, planningCenterCheckBox, tableModel, cityHeader);
		searchButton.addActionListener(actions);

		// Action Listener for the Export Button
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CSVExporter.exportTableDataToCSV(tableModel, cityField.getText());
			}
		});

		// Display the frame
		frame.setVisible(true);
	}
}