package churchDetailsFetcher.panels;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import churchDetailsFetcher.panels.tabPanels.CityPanel;
import churchDetailsFetcher.panels.tabPanels.HistoryPanel;
import churchDetailsFetcher.panels.tabPanels.StatePanel;
import churchDetailsFetcher.panels.tabPanels.UnknownPanel;

public class TabbedLeftPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private CityPanel cityPanel; // Instance variable
    private StatePanel statePanel;

    public TabbedLeftPanel() {
        setLayout(new BorderLayout());

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Initialize the instance variable instead of creating a new local variable
        cityPanel = new CityPanel(); // Assign to the instance variable
        statePanel = new StatePanel(); // New State panel
        UnknownPanel unknownPanel = new UnknownPanel(); // New Unknown panel
        HistoryPanel historyPanel = new HistoryPanel(); // New History panel

        // Add tabs to the tabbed pane, making CityPanel the default
        tabbedPane.addTab("City", cityPanel);
        tabbedPane.addTab("State", statePanel);
        tabbedPane.addTab("Unknown", unknownPanel);
        tabbedPane.addTab("History", historyPanel);

        // Set the default tab to the City tab
        tabbedPane.setSelectedIndex(0);

        // Add the tabbed pane to the main panel
        add(tabbedPane, BorderLayout.CENTER);
    }

    public CityPanel getCityPanel() {
        return cityPanel; // Return the instance of CityPanel
    }

    public StatePanel getStatePanel() {
        return statePanel; // Return the instance of CityPanel
    }

    // You may want to add methods to access components in the CityPanel
}