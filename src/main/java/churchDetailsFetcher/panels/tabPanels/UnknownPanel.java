package churchDetailsFetcher.panels.tabPanels;

import java.util.List;

import javax.swing.*;

import org.openqa.selenium.Dimension;

import churchDetailsFetcher.apiClients.LocationData;
import churchDetailsFetcher.types.OverPassApiData;

public class UnknownPanel extends JPanel {
    public UnknownPanel() {

        JButton button = new JButton("Test");

        JFrame frame = new JFrame();

        add(button);

        button.addActionListener(e -> {

            System.out.println("threads " + Runtime.getRuntime().availableProcessors());
            // List<OverPassApiData.Element> cities = LocationData.getCitiesForState("South
            // Africa", "Free State");

            // System.out.println("Done ");
            // for (OverPassApiData.Element city : cities) {

            // System.out.println(city.getTags().getName());
            // }

        });
    }
}
