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
            System.out.println("Se generaron las estadísticas desde OracleDB");
        } catch (SQLException ex) {
            System.out.println("Hubo un error de SQL: " + ex);
        }
        try {
            this.estadisticaDAO.storeInMongo();
            System.out.println("Se guardaron las estadísticas en MongoDB");
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
        try {
            this.estadisticaDAO.generateHistoricoVentas();
            System.out.println("Se generó el histórico de ventas");
        } catch (SQLException ex) {
            System.out.println("Hubo un error de SQL: " + ex);
        }
        try {
            this.estadisticaDAO.clearVentas();
            System.out.println("Se limpiaron los varrays de ventas de empleados");
        } catch (SQLException ex) {
            System.out.println("Hubo un error de SQL: " + ex);
        }
    }
}
