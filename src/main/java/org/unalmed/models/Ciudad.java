package org.unalmed.models;

public class Ciudad {
    private int cod;
    private String nom;
    private Departamento midep;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Departamento getMidep() {
        return midep;
    }

    public void setMidep(Departamento midep) {
        this.midep = midep;
    }
}
