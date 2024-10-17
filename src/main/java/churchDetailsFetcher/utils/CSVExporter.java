package churchDetailsFetcher.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import churchDetailsFetcher.types.ChurchDataTableModel;

public class CSVExporter {

    // Now accepts the JFrame to center the dialog
    public static void exportTableDataToCSV(ChurchDataTableModel tableModel, String cityName, JFrame parentFrame) {
        String defaultPath = System.getProperty("user.home") + File.separator + "Documents";

        // Ask the user for a file name, centering the dialog on the parent frame
        String inputFileName = JOptionPane.showInputDialog(parentFrame, "Enter file name:", "Save as", JOptionPane.PLAIN_MESSAGE);
        
        // If the user canceled or entered an empty file name, return without saving
        if (inputFileName == null || inputFileName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "File name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Append date and unique ID to the file name
        String finalFileName = generateFinalFileName(inputFileName, cityName);
        File fileToSave = new File(defaultPath, finalFileName);

        // Check if the file already exists and prompt the user for overwriting
        if (fileToSave.exists()) {
            int response = JOptionPane.showConfirmDialog(parentFrame, 
                    "File already exists. Overwrite?", "Confirm Overwrite", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response != JOptionPane.YES_OPTION) {
                return;  // User chose not to overwrite, so stop saving
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
            // Write column headers
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                writer.write(tableModel.getColumnName(i));
                if (i < tableModel.getColumnCount() - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // Write table data
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(String.valueOf(tableModel.getValueAt(i, j)));
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            // Show success message, centered on the parent frame
            JOptionPane.showMessageDialog(parentFrame,
                    "Data exported successfully!\nSaved to: \n" + fileToSave.getAbsolutePath());
        } catch (IOException ex) {
            // Show error message, centered on the parent frame
            JOptionPane.showMessageDialog(parentFrame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Generates the final file name with user input, date, and unique ID
    private static String generateFinalFileName(String inputFileName, String cityName) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        long uniqueID = System.currentTimeMillis() % 100000;  // Shorten the ID

        // Sanitize the file name input (remove illegal characters if necessary)
        inputFileName = inputFileName.trim().replaceAll("[^a-zA-Z0-9_\\-]", "_");

        return inputFileName + "_" + formattedDate + "_" + uniqueID + ".csv";
    }
}