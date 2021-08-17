package org.unalmed.controllers;

import java.io.IOException;
import java.sql.SQLException;

import com.mongodb.MongoBulkWriteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.unalmed.App;
import org.unalmed.daos.EstadisticaDAO;

public class HomeController {
    private EstadisticaDAO estadisticaDAO;
    public HomeController() {
        this.estadisticaDAO = new EstadisticaDAO();
    }

    @FXML
    private void generateStats(ActionEvent event) throws IOException {
        try {
            this.estadisticaDAO.generate();
        } catch (SQLException ex) {
            System.out.println("Hubo un error de SQL: " + ex);
        }
        try {
            this.estadisticaDAO.storeInMongo();
        } catch (MongoBulkWriteException ex) {
            System.out.println("Hubo un error insertando los datos: " + ex);
        }
    }

    @FXML
    private void switchToViewStats(ActionEvent event) throws IOException {
        App.setRoot("view");
    }

    @FXML
    private void clearStats(ActionEvent event) throws IOException {
        App.setRoot("");
    }
}
