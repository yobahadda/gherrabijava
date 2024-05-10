package com.ayoub.gherabijava.models;

public class Client {
    private String prenom;
    private String nom;
    private String adresse;
    private String email;
    private int telephone;

    public Client(String prenom, String nom, String adresse, String email, int telephone) {
        this.prenom = prenom;
        this.nom = nom;
        this.adresse = adresse;
        this.email = email;
        this.telephone = telephone;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getEmail() {
        return email;
    }

    public int getTelephone() {
        return telephone;
    }

}