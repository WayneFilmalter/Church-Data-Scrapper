package churchDetailsFetcher.errorrMessages;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class InvalidErrorMessage {
	
	public static void showInvalidCityPopup(JFrame frame, JTextField cityField) {
        // Display the popup message
        int result = JOptionPane.showOptionDialog(
            frame,
            "The entered city is invalid. Please enter a valid city.",
            "City Not Found",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            new Object[] { "Retry" },
            "Retry"
        );

        // If the user clicks "Retry", clear the city field and set focus
        if (result == JOptionPane.OK_OPTION) {
            cityField.setText("");  // Clear the input
            cityField.requestFocus();  // Set focus back to the city input field
        }
    }
	

}
