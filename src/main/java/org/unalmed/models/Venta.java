package org.unalmed.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Venta {

    private ObjectId id;

    @BsonProperty(value = "nombre_ciudad")
    private String nombreCiudad;

    @BsonProperty(value = "total_ciudad")
    private String totalCiudad;

    @BsonProperty(value = "cc_vendedor")
    private String ccVendedor;

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

    public String getTotalCiudad() {
        return totalCiudad;
    }

    public void setTotalCiudad(String totalCiudad) {
        this.totalCiudad = totalCiudad;
    }

    public String getCcVendedor() {
        return ccVendedor;
    }

    public void setCcVendedor(String ccVendedor) {
        this.ccVendedor = ccVendedor;
    }
}
