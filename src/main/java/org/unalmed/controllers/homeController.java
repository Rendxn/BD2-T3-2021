package org.unalmed.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.unalmed.App;

public class homeController {
    public homeController() {
    }

    @FXML
    private void switchQuery(ActionEvent event) throws IOException {
        App.setRoot("");
    }

    @FXML
    private void switchRegistrySeller(ActionEvent event) throws IOException {
        App.setRoot("");
    }

    @FXML
    private void switchRegistryProfit(ActionEvent event) throws IOException {
        App.setRoot("");
    }
}
