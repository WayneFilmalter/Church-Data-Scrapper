package churchDetailsFetcher;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

// TODO: Add autocomplete api or hardcode in xml localy

public class AutoCompleteCityComboBox {

    private JComboBox<String> cityComboBox;
    private JTextComponent editor;

    public AutoCompleteCityComboBox(JComboBox<String> cityComboBox) {
        this.cityComboBox = cityComboBox;
        this.cityComboBox.setEditable(true); // Allow typing into the JComboBox
        this.editor = (JTextComponent) cityComboBox.getEditor().getEditorComponent();
        
        // Add KeyListener to trigger auto-complete on typing
        this.editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = editor.getText();
                if (input.length() > 2) {
                    List<String> cities = fetchCitiesFromAPI(input); // Replace with your API or database call
                    updateCityComboBox(cities);
                }
            }
        });
    }

    // Method to update the JComboBox with new city data
    private void updateCityComboBox(List<String> cities) {
        cityComboBox.removeAllItems(); // Clear existing items
        for (String city : cities) {
            cityComboBox.addItem(city); // Add the cities dynamically
        }
        cityComboBox.showPopup(); // Show the drop-down with updated suggestions
    }

    // Stub method for fetching cities (replace this with real API call)
    private List<String> fetchCitiesFromAPI(String query) {
        // In a real application, you would call an API or database here
        // For now, we return a mock list of cities based on the input
        return List.of(query + " City 1", query + " City 2", query + " City 3");
    }
}