package churchDetailsFetcher.panels.tabPanels;

import javax.swing.*;

import org.openqa.selenium.Dimension;

import churchDetailsFetcher.apiClients.LocationData;

public class UnknownPanel extends JPanel {
    public UnknownPanel() {

        JButton button = new JButton("Test");

        JFrame frame = new JFrame();

        add(button);

        button.addActionListener(e -> {
            System.out.println("Start ");
            // String[] cities = LocationData.getCitiesForState(frame, "South Africa", "Free
            // State");

            System.out.println("Done ");
            // for (String city : cities) {

            // System.out.println(city.toString());
            // }
        });
    }
}
