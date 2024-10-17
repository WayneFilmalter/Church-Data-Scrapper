package churchDetailsFetcher;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import churchDetailsFetcher.utils.CSVExporter;

public class Main {

	public static void main(String[] args) {
		// Set global font
		Font globalFont = new Font("Arial", Font.PLAIN, 18);
		UIManager.put("Label.font", globalFont);
		UIManager.put("Button.font", globalFont);
		UIManager.put("TextField.font", globalFont);
		UIManager.put("CheckBox.font", globalFont);
		UIManager.put("Table.font", globalFont);
		UIManager.put("TableHeader.font", globalFont);

		// Create the main frame
		JFrame frame = new JFrame("Church Finder");
		frame.setSize(1400, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create a split pane for dividing the window
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(300);
		frame.add(splitPane);

		// Add Left Panel (input form)
		LeftSidePanel leftPanel = new LeftSidePanel();
		splitPane.setLeftComponent(leftPanel);

		// Add Right Panel (results table and buttons)
		RightSidePanel rightPanel = new RightSidePanel(frame);
		splitPane.setRightComponent(rightPanel);

		// Add functionality to the search button
		leftPanel.getSearchButton()
				.addActionListener(new ChurchFinder(leftPanel, rightPanel.getTableModel(), rightPanel.getCityHeader()));

		// Action Listener for the Export Button
		rightPanel.getExportButton().addActionListener(
				e -> CSVExporter.exportTableDataToCSV(rightPanel.getTableModel(), leftPanel.getCityFieldText(), frame));

		// Display the frame
		frame.setVisible(true);
	}
}