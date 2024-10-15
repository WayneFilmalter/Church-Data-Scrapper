package churchDetailsFetcher.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CSVExporter {

	public static void exportTableDataToCSV(DefaultTableModel tableModel, String cityName) {

		String defaultPath = System.getProperty("user.home") + File.separator + "Documents";
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
					"Data exported successfully!\nSaved to: \n" + fileToSave.getAbsolutePath());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
		}
	}

	private static String generateDefaultFileName(String cityName) {
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return cityName + "_" + formattedDate + ".csv";
	}
}