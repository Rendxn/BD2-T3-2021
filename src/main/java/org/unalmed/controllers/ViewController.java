package org.unalmed.controllers;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.unalmed.App;
import org.unalmed.daos.EstadisticaDAO;
import org.unalmed.models.Estadistica;

public class ViewController {
    EstadisticaDAO estadisticaDAO;
    List<Estadistica> estadisticas;
    public ViewController() {
        this.estadisticaDAO = new EstadisticaDAO();
        this.estadisticas = this.estadisticaDAO.getEstadisticas();
        System.out.println(this.estadisticas);
    }

    @FXML
    private void switchHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }
}
