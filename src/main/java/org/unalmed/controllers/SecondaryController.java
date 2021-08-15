package org.unalmed.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import org.unalmed.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}