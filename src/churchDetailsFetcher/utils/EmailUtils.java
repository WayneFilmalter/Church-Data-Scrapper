package churchDetailsFetcher.utils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.stream.Collectors;

import churchDetailsFetcher.types.DataShapes.EmailDetails;

public class EmailUtils {

    public static void copyEmailsToClipboard(List<? extends EmailDetails> emailDetails) {
        if (emailDetails.isEmpty()) {
            return; 
        }

        String emailString = emailDetails.stream()
                .map(EmailDetails::getEmail)
                .collect(Collectors.joining(" ")); 

        StringSelection stringSelection = new StringSelection(emailString);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }
}