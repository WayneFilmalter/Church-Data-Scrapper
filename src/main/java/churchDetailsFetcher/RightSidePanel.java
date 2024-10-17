package churchDetailsFetcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import churchDetailsFetcher.types.ChurchDataTableModel;
import churchDetailsFetcher.types.ChurchTableData;
import churchDetailsFetcher.types.DataTypes.NameEmail;
import churchDetailsFetcher.types.DataTypes.NamePhoneNumber;
import churchDetailsFetcher.utils.CSVExporter;
import churchDetailsFetcher.utils.EmailUtils;
import churchDetailsFetcher.utils.PhoneNumberUtils;

public class RightSidePanel extends JPanel {

	private static final long serialVersionUID = 8191814328511041694L;

	private ChurchDataTableModel tableModel;
	private JLabel cityHeader;
	private JButton exportButton, webScrapingButton, saveNumbersButton, copyEmailsButton, getPCOChurchesButton;

	public RightSidePanel(JFrame frame) {
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

		// Add ActionListener to Web Scraping button to show the selection dialog
		webScrapingButton.addActionListener(e -> showFunctionSelectionDialog(frame));

		exportButton.addActionListener(e -> CSVExporter.exportTableDataToCSV(tableModel, cityHeader.getName(), frame));

		saveNumbersButton.addActionListener(e -> {
			List<NamePhoneNumber> phoneNumbers = tableModel.getNumbers();
			if (!phoneNumbers.isEmpty()) {
				PhoneNumberUtils.exportPhoneNumbersToFile(phoneNumbers, "kdkd");
			} else {
				System.out.println("No phone numbers to export.");
			}
		});

		copyEmailsButton.addActionListener(e -> {
			List<NameEmail> emails = tableModel.getEmails();
			if (!emails.isEmpty()) {
				EmailUtils.copyEmailsToClipboard(emails);
			} else {
				System.out.println("No emails to copy.");
			}
		});

		getPCOChurchesButton.addActionListener(e -> {
			// Check if PCO checking is done
			if (tableModel.getCheckPCOdone()) {
				// Filter the list to include only churches with hasPCO set to true
				List<ChurchTableData> allChurches = tableModel.getTableData();
				List<ChurchTableData> pcoChurches = allChurches.stream().filter(ChurchTableData::getHasPCO).toList();
				tableModel.setTableData(pcoChurches);
				tableModel.fireTableDataChanged();
				// Display the filtered list of churches (you might want to show this in a
				// dialog or a table)
				if (pcoChurches.isEmpty()) {
					System.out.println("No churches found with Planning Center links.");
				} else {
					System.out.println("Churches with Planning Center links:");
					pcoChurches.forEach(church -> System.out.println(church.getName()));
				}
			} else {
				// Run the PCO check if not done
				CheckPlanningCenter.checkPlanningCenterLinks(tableModel, frame); // Replace parentComponent with the
																					// actual parent component
			}
		});

		getPCOChurchesButton.addActionListener(e -> {
			List<ChurchTableData> allChurches = tableModel.getTableData();
			List<ChurchTableData> pcoChurches = allChurches.stream().filter(ChurchTableData::getHasPCO).toList();
			tableModel.setTableData(pcoChurches);
			tableModel.fireTableDataChanged();
		});
	}

	private void showFunctionSelectionDialog(JFrame frame) {
		// Create a modal dialog
		JDialog dialog = new JDialog(frame, "Select Functions to Run", true);
		dialog.setLayout(new BorderLayout());

		// Create checkboxes for each function
		JPanel checkboxPanel = new JPanel(new GridLayout(0, 1));
		JCheckBox findDenominationsCheck = new JCheckBox("Find Denominations", true); // Default checked
		JCheckBox scrapeEmailsCheck = new JCheckBox("Scrape Emails", true); // Default checked
		JCheckBox headlessScrapingCheck = new JCheckBox("Headless Web Scraping", true); // Default checked
		JCheckBox hunterEmailsCheck = new JCheckBox("Hunter Emails", true); // Default checked
		JCheckBox checkPCOLinksCheck = new JCheckBox("Check Planning Center Links", true); // Default checked

		checkboxPanel.add(findDenominationsCheck);
		checkboxPanel.add(scrapeEmailsCheck);
		checkboxPanel.add(headlessScrapingCheck);
		checkboxPanel.add(hunterEmailsCheck);
		checkboxPanel.add(checkPCOLinksCheck);

		// Create confirm and cancel buttons
		JPanel buttonPanel = new JPanel();
		JButton confirmButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);

		// Add checkboxes and buttons to the dialog
		dialog.add(new JLabel("Select the functions to run:"), BorderLayout.NORTH);
		dialog.add(checkboxPanel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		// Action listener for Confirm button
		confirmButton.addActionListener(e -> {
			dialog.dispose(); // Close the dialog

			// Boolean flags for each function
			boolean runFindDenominations = findDenominationsCheck.isSelected();
			boolean runScrapeEmails = scrapeEmailsCheck.isSelected();
			boolean runHeadlessScraping = headlessScrapingCheck.isSelected();
			boolean runHunterEmails = hunterEmailsCheck.isSelected();
			boolean runCheckPCOLinks = checkPCOLinksCheck.isSelected();

			// Run selected functions in sequence
			runWebScrapingFunctions(runFindDenominations, runScrapeEmails, runHeadlessScraping, runHunterEmails,
					runCheckPCOLinks, frame);
		});

		// Action listener for Cancel button
		cancelButton.addActionListener(e -> dialog.dispose());

		// Pack the dialog to adjust its size based on components
		dialog.pack();

		// Center the dialog relative to the frame
		dialog.setLocationRelativeTo(frame);

		// Display the dialog
		dialog.setVisible(true);
	}

	// Method to run selected functions based on user input
	private void runWebScrapingFunctions(boolean runFindDenominations, boolean runScrapeEmails,
			boolean runHeadlessScraping, boolean runHunterEmails, boolean runCheckPCOLinks, JFrame frame) {
		if (runFindDenominations) {
			FindDenominations.scrapeDenominations(tableModel, frame, () -> {
				runScrapeEmailsFunction(runScrapeEmails, runHeadlessScraping, runHunterEmails, runCheckPCOLinks, frame);
			});
		} else {
			runScrapeEmailsFunction(runScrapeEmails, runHeadlessScraping, runHunterEmails, runCheckPCOLinks, frame);
		}
	}

	// Helper method to run the scrape emails function
	private void runScrapeEmailsFunction(boolean runScrapeEmails, boolean runHeadlessScraping, boolean runHunterEmails,
			boolean runCheckPCOLinks, JFrame frame) {
		if (runScrapeEmails) {
			StaticWebScraper.scrapeEmails(tableModel, frame, () -> {
				runHeadlessScrapingFunction(runHeadlessScraping, runHunterEmails, runCheckPCOLinks, frame);
			});
		} else {
			runHeadlessScrapingFunction(runHeadlessScraping, runHunterEmails, runCheckPCOLinks, frame);
		}
	}

	// Helper method to run the headless scraping function
	private void runHeadlessScrapingFunction(boolean runHeadlessScraping, boolean runHunterEmails,
			boolean runCheckPCOLinks, JFrame frame) {
		if (runHeadlessScraping) {
			HeadlessWebScraper.scrapeEmails(tableModel, frame, () -> {
				runHunterEmailsFunction(runHunterEmails, runCheckPCOLinks, frame);
			});
		} else {
			runHunterEmailsFunction(runHunterEmails, runCheckPCOLinks, frame);
		}
	}

	// Helper method to run the hunter emails function
	private void runHunterEmailsFunction(boolean runHunterEmails, boolean runCheckPCOLinks, JFrame frame) {
		if (runHunterEmails) {
			EmailsFromHunter.getEmailsFromHunter(tableModel, frame, () -> {
				runCheckPCOLinksFunction(runCheckPCOLinks, frame);
			});
		} else {
			runCheckPCOLinksFunction(runCheckPCOLinks, frame);
		}
	}

	// Helper method to run the check PCO links function
	private void runCheckPCOLinksFunction(boolean runCheckPCOLinks, JFrame frame) {
		if (runCheckPCOLinks) {
			CheckPlanningCenter.checkPlanningCenterLinks(tableModel, frame);
		}
	}

	// Method to set the city header
	public void setCityHeader(String city) {
		cityHeader.setText(city);
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