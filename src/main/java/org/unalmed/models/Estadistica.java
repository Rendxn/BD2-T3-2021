package org.unalmed.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Estadistica {

    private ObjectId id;

    // Quiero poner mis_ventas, pero la guía poine misventas. Ugly T-T
    @BsonProperty(value = "misventas")
    private ArrayList<Venta> misVentas;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ArrayList<Venta> getMisVentas() {
        return misVentas;
    }

    public void setMisVentas(ArrayList<Venta> misVentas) {
        this.misVentas = misVentas;
    }
}
