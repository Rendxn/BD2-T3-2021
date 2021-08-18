package org.unalmed.models;

public class Departamento {
    private int cod;
    private String nom;
    private int totalVentas;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNom() {
        return nom;
    }

    public int getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(int ventas) {
        this.totalVentas = ventas;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "cod=" + cod +
                ", nom='" + nom + '\'' +
                ", ventas=" + totalVentas +
                '}';
    }

}
