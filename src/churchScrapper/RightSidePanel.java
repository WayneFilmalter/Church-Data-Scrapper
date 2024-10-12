package churchScrapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RightSidePanel extends JPanel {

	private static final long serialVersionUID = 8191814328511041694L;

	private DefaultTableModel tableModel;
	private JLabel cityHeader;
	private JButton exportButton, saveButton, copyNumbersButton;

	public RightSidePanel() {
		setLayout(new BorderLayout());

		// City header
		cityHeader = new JLabel("No Results", SwingConstants.CENTER);
		cityHeader.setFont(new Font("Arial", Font.BOLD, 20));
		add(cityHeader, BorderLayout.NORTH);

		// Table for results
		String[] columnNames = { "Name",  "Denomination", "Address", "Phonenumber", "Website" };
		tableModel = new DefaultTableModel(columnNames, 0);
		JTable resultsTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(resultsTable);
		add(scrollPane, BorderLayout.CENTER);

		// Buttons at the bottom
		JPanel buttonPanel = new JPanel();
		exportButton = new JButton("Export to CSV");
		saveButton = new JButton("Try Scrapping");
		copyNumbersButton = new JButton("Copy Numbers");
		
		// Set Button Size
        exportButton.setPreferredSize(new Dimension(150, 40));
        saveButton.setPreferredSize(new Dimension(150, 40));
        copyNumbersButton.setPreferredSize(new Dimension(150, 40));

		buttonPanel.add(exportButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(copyNumbersButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public JButton getExportButton() {
		return exportButton;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JLabel getCityHeader() {
		return cityHeader;
	}
}