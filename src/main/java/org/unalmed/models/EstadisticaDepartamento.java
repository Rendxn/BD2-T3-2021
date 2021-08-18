package org.unalmed.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

public class EstadisticaDepartamento {

    @BsonProperty("_id")
    private ObjectId id;
    private String departamento;

    @BsonProperty("misventas")
    private List<Venta> misVentas;

    @BsonProperty("mejor_vendedor")
    private Venta mejorVendedor;

    @BsonProperty("peor_vendedor")
    private Venta peorVendedor;

    @BsonProperty("total_departamento")
    private int totalDepartamento;

    @BsonProperty("mejor_ciudad")
    private Venta mejorCiudad;


    public int getTotalDepartamento() {
        return totalDepartamento;
    }

    public void setTotalDepartamento(int totalDepartamento) {
        this.totalDepartamento = totalDepartamento;
    }

    public EstadisticaDepartamento() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Venta> getMisVentas() {
        return misVentas;
    }

    public void setMisVentas(List<Venta> misVentas) {
        this.misVentas = misVentas;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Venta getMejorVendedor() {
        return mejorVendedor;
    }

    public void setMejorVendedor(Venta mejorVendedor) {
        this.mejorVendedor = mejorVendedor;
    }

    public Venta getPeorVendedor() {
        return peorVendedor;
    }

    public void setPeorVendedor(Venta peorVendedor) {
        this.peorVendedor = peorVendedor;
    }

    public Venta getMejorCiudad() {
        return mejorCiudad;
    }

    public void setMejorCiudad(Venta mejorCiudad) {
        this.mejorCiudad = mejorCiudad;
    }

    @Override
    public String toString() {
        return "Estadistica{" +
                "id=" + id +
                ", departamento='" + departamento + '\'' +
                ", misVentas=" + misVentas +
                ", mejorVendedor=" + mejorVendedor +
                ", peorVendedor=" + peorVendedor +
                ", totalDepartamento=" + totalDepartamento +
                ", mejorCiudad=" + mejorCiudad +
                '}';
    }
}
