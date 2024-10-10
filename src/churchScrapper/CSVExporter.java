package churchScrapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVExporter {

	// Method to export table data to a CSV file
	public static void exportTableDataToCSV(DefaultTableModel tableModel, String cityName) {
		// Generate the default file name and path
		String defaultPath = System.getProperty("user.home") + File.separator + "Documents"; // Default to Documents
																								// folder
		String defaultFileName = generateDefaultFileName(cityName);
		File fileToSave = new File(defaultPath, defaultFileName);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
			// Write column headers
			for (int i = 0; i < tableModel.getColumnCount(); i++) {
				writer.write(tableModel.getColumnName(i));
				if (i < tableModel.getColumnCount() - 1) {
					writer.write(",");
				}
			}
			writer.newLine();

			// Write data rows
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				for (int j = 0; j < tableModel.getColumnCount(); j++) {
					writer.write(String.valueOf(tableModel.getValueAt(i, j)));
					if (j < tableModel.getColumnCount() - 1) {
						writer.write(",");
					}
				}
				writer.newLine();
			}
			JOptionPane.showMessageDialog(null,
					"Data exported successfully!\nSaved to: " + fileToSave.getAbsolutePath());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
		}
	}

	// Method to generate a default file name based on the city name and current
	// date
	private static String generateDefaultFileName(String cityName) {
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return cityName + "_" + formattedDate + ".csv";
	}
}