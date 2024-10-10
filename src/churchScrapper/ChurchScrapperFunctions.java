package churchScrapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChurchScrapperFunctions implements ActionListener {
	private JTextField cityField;
	private JCheckBox emailCheckBox, phoneCheckBox, suburbCheckBox, streetCheckBox, nameCheckBox,
			planningCenterCheckBox;
	private DefaultTableModel tableModel;
	private JLabel cityHeader;

	public ChurchScrapperFunctions(JTextField cityField, JCheckBox emailCheckBox, JCheckBox phoneCheckBox,
			JCheckBox suburbCheckBox, JCheckBox streetCheckBox, JCheckBox nameCheckBox,
			JCheckBox planningCenterCheckBox, DefaultTableModel tableModel, JLabel cityHeader) {
		this.cityField = cityField;
		this.emailCheckBox = emailCheckBox;
		this.phoneCheckBox = phoneCheckBox;
		this.suburbCheckBox = suburbCheckBox;
		this.streetCheckBox = streetCheckBox;
		this.nameCheckBox = nameCheckBox;
		this.planningCenterCheckBox = planningCenterCheckBox;
		this.tableModel = tableModel;
		this.cityHeader = cityHeader;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String city = cityField.getText();

		if (city.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter a city name.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Update the city header
		cityHeader.setText("Results for: " + city);

		// Clear the table
		tableModel.setRowCount(0);

		// Mock data, replace with actual scraping logic
		String[] row1 = { "Suburb1", "Church A", "emailA@example.com", "123-456-7890", "Street 1", "Yes" };
		String[] row2 = { "Suburb2", "Church B", "emailB@example.com", "098-765-4321", "Street 2", "No" };
		tableModel.addRow(row1);
		tableModel.addRow(row2);
	}
}