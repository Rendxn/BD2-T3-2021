package org.unalmed.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import org.unalmed.App;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
