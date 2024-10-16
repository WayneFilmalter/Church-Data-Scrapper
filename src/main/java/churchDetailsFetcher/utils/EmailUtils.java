package churchDetailsFetcher.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import churchDetailsFetcher.types.DataTypes.NameEmail;

public class EmailUtils {

	// TODO: add popups for errors
	public static void copyEmailsToClipboard(List<? extends NameEmail> emailDetails) {
		// Check if the list is empty or contains only empty email strings
		if (emailDetails.isEmpty() || emailDetails.stream().allMatch(emailDetail -> emailDetail.getEmail().isEmpty())) {
			// Show a popup indicating no emails found
			JOptionPane.showMessageDialog(null, "No emails found.", "Information", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		// Collect emails into a single string
		String emailString = emailDetails.stream().map(NameEmail::getEmail).collect(Collectors.joining(" "));

		StringSelection stringSelection = new StringSelection(emailString);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
}