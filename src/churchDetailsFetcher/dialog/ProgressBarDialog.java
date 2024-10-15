package churchDetailsFetcher.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBarDialog extends JDialog {

    private static final long serialVersionUID = -9003966842902357658L;
    
    private JProgressBar progressBar;
    private JButton cancelButton;
    private JLabel loadingLabel;
    private JLabel statusLabel; // For displaying found count/total
    private boolean isCancelled = false;

    public ProgressBarDialog(JFrame parentFrame) {
        super(parentFrame, "Loading", true);
        setUndecorated(true);
        
        // Create components
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(500, 40)); // Progress bar size

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCancelled = true;
                dispose(); // Close the dialog
            }
        });

        loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        statusLabel = new JLabel("0 / 0"); // Initially set to 0 found / total
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add loading label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        add(loadingLabel, gbc);
        
        // Add progress bar
        gbc.gridy = 1; // Move to the next row
        gbc.weighty = 1.0; // Allow the progress bar to grow
        add(progressBar, gbc);
        
        // Add status label (found count/total)
        gbc.gridx = 1; // Move to the next column
        gbc.anchor = GridBagConstraints.EAST; // Align to the right
        add(statusLabel, gbc);
        
        // Add cancel button
        gbc.gridx = 0; // Reset to first column
        gbc.gridy = 2; // Move to the next row
        gbc.weighty = 0; // No vertical weight
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER; // Center button
        add(cancelButton, gbc);

        // Set dialog properties
        setPreferredSize(new Dimension(550, 150)); // Increased dialog size
        pack();
        setLocationRelativeTo(parentFrame); // Center on parent frame
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Prevent closing without clicking cancel
    }

    // Method to set the maximum value of the progress bar
    public void setProgressMax(int maxValue) {
        progressBar.setMaximum(maxValue);
    }

    // Method to update the progress bar's value and status label
    public void updateProgress(int progress, int foundCount, int totalCount) {
        progressBar.setValue(progress);
        int percentage = (int) ((progress / (double) progressBar.getMaximum()) * 100);
        progressBar.setString("Loading... " + percentage + "%");
        statusLabel.setText(foundCount + " / " + totalCount); // Update found count/total
    }

    // Method to check if the operation was cancelled
    public boolean isCancelled() {
        return isCancelled;
    }
}