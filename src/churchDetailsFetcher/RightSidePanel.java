package churchDetailsFetcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import churchDetailsFetcher.utils.EmailUtils;
import churchDetailsFetcher.utils.PhoneNumberUtils;

public class RightSidePanel extends JPanel {

	private static final long serialVersionUID = 8191814328511041694L;

	private DefaultTableModel tableModel;
	private JLabel cityHeader;
	private JButton exportButton, webScrapingButton, saveNumbersButton, copyEmailsButton, getPCOChurchesButton;

	private JDialog createLoadingDialog(JFrame parentFrame) {
		JDialog loadingDialog = new JDialog(parentFrame, "Loading", true);
		JLabel loadingLabel = new JLabel("Loading, please wait...", SwingConstants.CENTER);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		loadingDialog.setLayout(new BorderLayout());
		loadingDialog.add(loadingLabel, BorderLayout.NORTH);
		loadingDialog.add(progressBar, BorderLayout.CENTER);
		loadingDialog.setSize(300, 100);
		loadingDialog.setLocationRelativeTo(parentFrame);
		loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		return loadingDialog;
	}

	public RightSidePanel() {
		setLayout(new BorderLayout());

		// City header
		cityHeader = new JLabel("No Results", SwingConstants.CENTER);
		cityHeader.setFont(new Font("Arial", Font.BOLD, 20));
		add(cityHeader, BorderLayout.NORTH);

		// Table for results
		String[] columnNames = { "Name", "Denomination", "Address", "Phonenumber", "Website", "emails" };
		tableModel = new DefaultTableModel(columnNames, 0);
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

		// Add action listeners for the buttons
//        webScrapingButton.addActionListener(e -> ChurchScraper.scrapeWithConfirmation(parentFrame, getChurchDataFromTable()));

//		copyEmailsButton.addActionListener(e -> EmailUtils.copyEmailsToClipboard());
//
//		saveNumbersButton.addActionListener(e -> PhoneNumberUtils.exportPhoneNumbersToFile());
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