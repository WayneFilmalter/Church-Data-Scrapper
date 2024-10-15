package churchDetailsFetcher.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;

import churchDetailsFetcher.dialog.SuccessDialog;
import churchDetailsFetcher.types.DataTypes.NamePhoneNumber;

public class PhoneNumberUtils {

	public static void exportPhoneNumbersToFile(List<? extends NamePhoneNumber> contactDetails, String cityName) {

		if (contactDetails.isEmpty()) {
			// Show a popup indicating no phone numbers found

			// TODO: make this a temp popup
			JOptionPane.showMessageDialog(null, "No phone numbers found.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		String filePath = Paths.get(System.getProperty("user.home"), "Documents",
				"phonenumbers-" + cityName + "-" + currentDate + ".txt").toString();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			int nameWidth = 60;
			int phoneWidth = 15;

			for (NamePhoneNumber contactable : contactDetails) {
				String formattedLine = String.format("%-" + nameWidth + "s : %" + phoneWidth + "s",
						contactable.getName(), contactable.getPhoneNumber());
				writer.write(formattedLine);
				writer.newLine();
			}

			// Show success message
			SuccessDialog.showSuccessPopup("Phone numbers exported successfully!\nSaved to: " + filePath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}