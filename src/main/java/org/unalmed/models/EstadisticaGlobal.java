package org.unalmed.models;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class EstadisticaGlobal {
    @BsonProperty("mejor_vendedor")
    private Venta mejorVendedor;

    @BsonProperty("peor_vendedor")
    private Venta peorVendedor;

    @BsonProperty("mejor_ciudad")
    private Venta mejorCiudad;

    @BsonProperty("mejor_departamento")
    private Departamento mejorDepartamento;

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

    public Departamento getMejorDepartamento() {
        return mejorDepartamento;
    }

    public void setMejorDepartamento(Departamento mejorDepartamento) {
        this.mejorDepartamento = mejorDepartamento;
    }

    @Override
    public String toString() {
        return "EstadisticaGlobal{" +
                "mejorVendedor=" + mejorVendedor +
                ", peorVendedor=" + peorVendedor +
                ", mejorCiudad=" + mejorCiudad +
                ", mejorDepartamento=" + mejorDepartamento +
                '}';
    }
}
