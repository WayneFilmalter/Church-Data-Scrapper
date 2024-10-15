package churchDetailsFetcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;

public class RightSidePanel extends JPanel {

	private static final long serialVersionUID = 8191814328511041694L;

	private ChurchDataTableModel tableModel; 
	private JLabel cityHeader;
	private JButton exportButton, webScrapingButton, saveNumbersButton, copyEmailsButton, getPCOChurchesButton;

	public RightSidePanel() {
		setLayout(new BorderLayout());

		// City header
		cityHeader = new JLabel("No Results", SwingConstants.CENTER);
		cityHeader.setFont(new Font("Arial", Font.BOLD, 20));
		add(cityHeader, BorderLayout.NORTH);

		tableModel = new ChurchDataTableModel(new ArrayList<ChurchTableData>());
		JTable resultsTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(resultsTable);
		add(scrollPane, BorderLayout.CENTER);

		// Buttons at the bottom
		JPanel buttonPanel = new JPanel();
		exportButton = new JButton("Export to CSV");
		webScrapingButton = new JButton("Try Scraping");
		saveNumbersButton = new JButton("Save Numbers");
		copyEmailsButton = new JButton("Copy Emails");
		getPCOChurchesButton = new JButton("Get PCO Churches");

		// Set Button Size
		exportButton.setPreferredSize(new Dimension(180, 40));
		webScrapingButton.setPreferredSize(new Dimension(180, 40));
		saveNumbersButton.setPreferredSize(new Dimension(180, 40));
		copyEmailsButton.setPreferredSize(new Dimension(180, 40));
		getPCOChurchesButton.setPreferredSize(new Dimension(180, 40));

		buttonPanel.add(exportButton);
		buttonPanel.add(webScrapingButton);
		buttonPanel.add(saveNumbersButton);
		buttonPanel.add(copyEmailsButton);
		buttonPanel.add(getPCOChurchesButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public JButton getExportButton() {
		return exportButton;
	}

	public ChurchDataTableModel getTableModel() {
		return tableModel; 
	}

	public JLabel getCityHeader() {
		return cityHeader;
	}

	public JButton getCopyEmailsButton() {
		return copyEmailsButton;
	}

	public JButton getFindPCOChurchesButton() {
		return getPCOChurchesButton;
	}

	public JButton getWebScrapingButton() {
		return webScrapingButton;
	}

	public JButton getSaveNumbersButton() {
		return saveNumbersButton;
	}
}