package churchDetailsFetcher.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import churchDetailsFetcher.types.DataShapes.PhoneNumberDetails;

public class PhoneNumberUtils {

	public static void exportPhoneNumbersToFile(List<? extends PhoneNumberDetails> contactDetails, String cityName) {

		String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

		String filePath = Paths.get(System.getProperty("user.home"), "Documents",
				"phonenumbers-" + cityName + "-" + currentDate + ".txt").toString();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (PhoneNumberDetails contactable : contactDetails) {
				writer.write(contactable.getName() + " : " + contactable.getPhoneNumber());
				writer.newLine();
			}
			System.out.println("Phone numbers exported to: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}