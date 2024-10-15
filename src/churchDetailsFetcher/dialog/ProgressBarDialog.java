package churchDetailsFetcher.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBarDialog extends JDialog {

    private static final long serialVersionUID = -9003966842902357658L;
    
	private JProgressBar progressBar;
    private JButton cancelButton;
    private boolean isCancelled = false;

    public ProgressBarDialog(JFrame parentFrame) {
        super(parentFrame, "Loading", true);
        
        setUndecorated(true);

        // Create progress bar with a larger size
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(500, 40)); // Increased size
        progressBar.setStringPainted(true);

        // Create cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 40)); // Match button size to progress bar
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancelled = true;
                dispose(); // Close the dialog
            }
        });

        // Layout
        setLayout(new BorderLayout());
        add(progressBar, BorderLayout.CENTER);
        add(cancelButton, BorderLayout.SOUTH);

        // Increase dialog size and center on parent frame
        setPreferredSize(new Dimension(550, 150)); // Increased dialog size
        pack();
        setLocationRelativeTo(parentFrame); // Center on parent frame
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Prevent closing without clicking cancel
    }

    // Method to set the maximum value of the progress bar
    public void setProgressMax(int maxValue) {
        progressBar.setMaximum(maxValue);
    }

    // Method to update the progress bar's value
    public void updateProgress(int progress) {
        progressBar.setValue(progress);
        int percentage = (int) ((progress / (double) progressBar.getMaximum()) * 100);
        progressBar.setString("Loading... " + percentage + "%");
    }

    // Method to check if the operation was cancelled
    public boolean isCancelled() {
        return isCancelled;
    }
}