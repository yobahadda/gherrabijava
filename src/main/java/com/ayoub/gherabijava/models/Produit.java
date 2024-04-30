package com.ayoub.gherabijava.models;

public class Produit {

    private String ID_produit;
    private String Nom;
    private String Description;
    private Double Prix;

    public Produit(String ID_produit, String nom, String description, Double prix) {
        this.ID_produit = ID_produit;
        Nom = nom;
        Description = description;
        Prix = prix;
    }

    public String getID_produit() {
        return ID_produit;
    }

    public void setID_produit(String ID_produit) {
        this.ID_produit = ID_produit;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getPrix() {
        return Prix;
    }

    public void setPrix(Double prix) {
        Prix = prix;
    }
}