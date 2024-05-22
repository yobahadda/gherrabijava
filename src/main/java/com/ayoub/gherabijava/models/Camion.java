package com.ayoub.gherabijava.models;

public class Camion {
    private int id;
    private String matricule;
    private String adresse;
    private float poidsTotal;
    private float poidsCourant;

    public Camion(int id, String matricule, String adresse, float poidsTotal, float poidsCourant) {
        this.id = id;
        this.matricule = matricule;
        this.adresse = adresse;
        this.poidsTotal = poidsTotal;
        this.poidsCourant = poidsCourant;
    }

    public int getId() {
        return id;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getAdresse() {
        return adresse;
    }

    public float getPoidsTotal() {
        return poidsTotal;
    }

    public float getPoidsCourant() {
        return poidsCourant;
    }
}
