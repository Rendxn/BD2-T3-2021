package org.unalmed.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import org.unalmed.App;
import org.unalmed.daos.EstadisticaDAO;
import org.unalmed.models.EstadisticaDepartamento;
import org.unalmed.models.EstadisticaGlobal;


public class ViewController implements Initializable {

    EstadisticaDAO estadisticaDAO;
    List<EstadisticaDepartamento> estadisticasDepartamento;
    EstadisticaGlobal estadisticasGlobales;
    List<EstadisticaGlobal> estadisticasGlobalesAux = new ArrayList<>();


    public ViewController() {
        this.estadisticaDAO = new EstadisticaDAO();
        this.estadisticasDepartamento = this.estadisticaDAO.getEstadisticasDepartamentos();
        this.estadisticasGlobales = this.estadisticaDAO.getEstadisticasGlobales();
        estadisticasGlobalesAux.add(this.estadisticasGlobales);


    }


    @FXML
    private TableView<EstadisticaDepartamento> statsDept;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> departamento;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> totalVentasDepartamento;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> ciudadMasVentas;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> totalCiudadMasVentas;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> mayorVendedor;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> ventasMayorVendedor;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> menorVendedor;

    @FXML
    private TableColumn<EstadisticaDepartamento, String> ventasMenorVendedor;

    @FXML
    private TableView<EstadisticaGlobal> statsGlobal;

    @FXML
    private TableColumn<EstadisticaGlobal, String> departamentoMasVentas;

    @FXML
    private TableColumn<EstadisticaGlobal, String> totalVentasDepartamentoGlobal;

    @FXML
    private TableColumn<EstadisticaGlobal, String> ciudadMasVentasGlobal;

    @FXML
    private TableColumn<EstadisticaGlobal, String> ventasCiudadGlobal;

    @FXML
    private TableColumn<EstadisticaGlobal, String> mayorVendedorGlobal;

    @FXML
    private TableColumn<EstadisticaGlobal, String> ventasMayorVendedorGlobal;

    @FXML
    private TableColumn<EstadisticaGlobal, String> menorVendedorGlobal;

    @FXML
    private TableColumn<EstadisticaGlobal, String> ventasMenorVendedorGlobal;

    @FXML
    private ObservableList<EstadisticaDepartamento> departamentosObservable;


    @FXML
    private ObservableList<EstadisticaGlobal> globalObservable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        statsDept.setItems(FXCollections.observableList(this.estadisticasDepartamento));
        departamento.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getDepartamento()));
        totalVentasDepartamento.setCellValueFactory(celda -> new SimpleStringProperty(Integer.toString(celda.getValue().getTotalDepartamento())));
        ciudadMasVentas.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getMejorCiudad().getNombreCiudad()));
        totalCiudadMasVentas.setCellValueFactory(celda -> new SimpleStringProperty(Integer.toString(celda.getValue().getMejorCiudad().getTotalCiudad())));
        mayorVendedor.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getMejorVendedor().getCcVendedor()));
        ventasMayorVendedor.setCellValueFactory(celda -> new SimpleStringProperty(Integer.toString(celda.getValue().getMejorVendedor().getTotalVendedor())));
        menorVendedor.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getPeorVendedor().getCcVendedor()));
        ventasMenorVendedor.setCellValueFactory(celda -> new SimpleStringProperty(Integer.toString(celda.getValue().getPeorVendedor().getTotalVendedor())));
        departamentosObservable = FXCollections.observableList(estadisticasDepartamento);

        statsGlobal.setItems(FXCollections.observableList(this.estadisticasGlobalesAux));
        departamentoMasVentas.setCellValueFactory(celda -> new SimpleStringProperty(
                celda.getValue().getMejorDepartamento() != null
                        ? celda.getValue().getMejorDepartamento().getNom() : ""));
        totalVentasDepartamentoGlobal.setCellValueFactory(celda -> new SimpleStringProperty(
                Integer.toString(celda.getValue().getMejorDepartamento() != null ? celda.getValue().getMejorDepartamento().getTotalVentas() : 0)));
        ciudadMasVentasGlobal.setCellValueFactory(celda -> new SimpleStringProperty(
                celda.getValue().getMejorCiudad() != null ? celda.getValue().getMejorCiudad().getNombreCiudad() : ""));
        ventasCiudadGlobal.setCellValueFactory(celda -> new SimpleStringProperty(
                Integer.toString(celda.getValue().getMejorCiudad() != null ? celda.getValue().getMejorCiudad().getTotalCiudad() : 0)));
        mayorVendedorGlobal.setCellValueFactory(celda -> new SimpleStringProperty(
                celda.getValue().getMejorVendedor() != null ? celda.getValue().getMejorCiudad().getCcVendedor(): ""));
        ventasMayorVendedorGlobal.setCellValueFactory(celda -> new SimpleStringProperty(
                Integer.toString(celda.getValue().getMejorVendedor() !=null ? celda.getValue().getMejorVendedor().getTotalVendedor(): 0)));
        menorVendedorGlobal.setCellValueFactory(celda -> new SimpleStringProperty(
                celda.getValue().getPeorVendedor() !=null ? celda.getValue().getPeorVendedor().getCcVendedor(): ""));
        ventasMenorVendedorGlobal.setCellValueFactory(celda -> new SimpleStringProperty(
                Integer.toString(celda.getValue().getPeorVendedor() !=null ? celda.getValue().getPeorVendedor().getTotalVendedor(): 0)));
        globalObservable = FXCollections.observableList(estadisticasGlobalesAux);

        System.out.println("Estadisticas por departamento: ");
        System.out.println(estadisticasDepartamento);
        System.out.println("Estadisticas globales: ");
        System.out.println(estadisticasGlobales);
    }

    @FXML
    private void switchHome(ActionEvent event) throws IOException {
        App.setRoot("home");
    }
}
