package org.unalmed.models;

import java.util.ArrayList;

public class Empleado {
    private int cc;
    private String nom;
    private Ciudad miciu;
    private ArrayList<Venta> ventas;

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Ciudad getMiciu() {
        return miciu;
    }

    public void setMiciu(Ciudad miciu) {
        this.miciu = miciu;
    }

    public ArrayList<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(ArrayList<Venta> ventas) {
        this.ventas = ventas;
    }
}
