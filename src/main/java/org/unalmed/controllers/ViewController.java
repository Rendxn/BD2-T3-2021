package org.unalmed.controllers;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.unalmed.App;
import org.unalmed.daos.EstadisticaDAO;
import org.unalmed.models.EstadisticaDepartamento;
import org.unalmed.models.EstadisticaGlobal;

public class ViewController {
    EstadisticaDAO estadisticaDAO;
    List<EstadisticaDepartamento> estadisticasDepartamento;
    List<EstadisticaGlobal> estadisticasGlobales;

    public ViewController() {
        this.estadisticaDAO = new EstadisticaDAO();
        this.estadisticasDepartamento = this.estadisticaDAO.getEstadisticasDepartamentos();
        System.out.println(this.estadisticasDepartamento);
    }

    @FXML
    private void switchHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }
}
