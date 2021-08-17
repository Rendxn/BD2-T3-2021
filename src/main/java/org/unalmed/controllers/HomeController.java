package org.unalmed.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.unalmed.App;

public class HomeController {
    public HomeController() {
    }

    @FXML
    private void generateStats(ActionEvent event) throws IOException {
        App.setRoot("generate");
    }

    @FXML
    private void switchToViewStats(ActionEvent event) throws IOException {
        App.setRoot("");
    }

    @FXML
    private void clearStats(ActionEvent event) throws IOException {
        App.setRoot("");
    }
}
