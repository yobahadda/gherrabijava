package com.ayoub.gherabijava.models;

public class ProduitFacture {
    String nom;
    int quantite;
    float montant;
    int commandeID;
    int produitID;
    float weight;
    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
    public ProduitFacture(String nom, int quantite, float montant) {
        this.nom = nom;
        this.quantite = quantite;
        this.montant = montant;
    }
    public ProduitFacture(String nom, int quantite, float montant, float weight) {
        this.nom = nom;
        this.quantite = quantite;
        this.montant = montant;
        this.weight = weight; // Initialize weight
    }

    public String getNom() {
        return nom;
    }

    public int getQuantite() {
        return quantite;
    }

    public float getMontant() {
        return montant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public int getCommandeID() {
        return commandeID;
    }

    public void setCommandeID(int commandeID) {
        this.commandeID = commandeID;
    }

    public int getProduitID() {
        return produitID;
    }

    public void setProduitID(int produitID) {
        this.produitID = produitID;
    }

    public int getID_produit() {
        return this.produitID;
    }
}
