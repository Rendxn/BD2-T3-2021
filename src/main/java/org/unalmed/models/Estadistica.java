package org.unalmed.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

public class Estadistica {

    @BsonProperty("_id")
    private ObjectId id;
    private String departamento;

    @BsonProperty("misventas")
    private List<Venta> misVentas;

    public Estadistica() {
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

    @Override
    public String toString() {
        return "Estadistica{" +
                "id=" + id +
                ", departamento='" + departamento + '\'' +
                ", misVentas=" + misVentas +
                '}';
    }
}
