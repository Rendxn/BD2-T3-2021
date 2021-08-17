package org.unalmed.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Venta {

    private ObjectId id;

    @BsonProperty(value = "nombre_ciudad")
    private String nombreCiudad;

    @BsonProperty(value = "total_ciudad")
    private int totalCiudad;

    @BsonProperty(value = "cc_vendedor")
    private String ccVendedor;

    @BsonProperty(value = "ventas_vendedor")
    private int totalVendedor;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public int getTotalCiudad() {
        return totalCiudad;
    }

    public void setTotalCiudad(int totalCiudad) {
        this.totalCiudad = totalCiudad;
    }

    public String getCcVendedor() {
        return ccVendedor;
    }

    public void setCcVendedor(String ccVendedor) {
        this.ccVendedor = ccVendedor;
    }

    public int getTotalVendedor() {
        return totalVendedor;
    }

    public void setTotalVendedor(int totalVendedor) {
        this.totalVendedor = totalVendedor;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", nombreCiudad='" + nombreCiudad + '\'' +
                ", totalCiudad=" + totalCiudad +
                ", ccVendedor='" + ccVendedor + '\'' +
                ", totalVendedor=" + totalVendedor +
                '}';
    }
}
