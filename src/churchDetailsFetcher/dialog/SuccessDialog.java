package churchDetailsFetcher.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer; // Use javax.swing.Timer

public class SuccessDialog {

    public static void showSuccessPopup(String message) {
        JOptionPane popup = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        final JDialog dialog = popup.createDialog(null, "Success");
        dialog.setModal(false); // Allows closing the dialog automatically after a while

        // Show the dialog
        dialog.setVisible(true);

        // Use a separate thread to close the dialog after 5 seconds
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Wait for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(dialog::dispose); // Close the dialog on the Event Dispatch Thread
        }).start();
    }

    public static void showNotificationDialog(Component parentComponent, String message) {
        // Create an undecorated JDialog
        JDialog notificationDialog = new JDialog((JFrame) null, true);
        notificationDialog.setUndecorated(true); // Remove window decorations
        notificationDialog.setSize(300, 100);

        // Create a label for the message
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Add the label to the dialog
        notificationDialog.setLayout(new BorderLayout());
        notificationDialog.add(messageLabel, BorderLayout.CENTER);

        // Center the dialog relative to the parent component
        notificationDialog.setLocationRelativeTo(parentComponent);

        // Timer to automatically close the dialog after 5 seconds
        Timer timer = new Timer(3000, e -> notificationDialog.dispose()); // Use a lambda for brevity
        timer.setRepeats(false); // Only run once
        timer.start();

        // Show the dialog
        notificationDialog.setVisible(true); // Show the dialog AFTER starting the timer
    }
}