package com.ayoub.gherabijava.models;

import java.sql.*;
import java.util.List;

public class Client {
    private int ClientID;

    public Client(int clientID, String nom, String adress, String email, int phoneNumber) {
    this.ClientID= clientID;
    this.Nom= nom;
    this.Adresse= adress;
    this.email= email;
    this.telephone= phoneNumber;
    }

    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int clientID) {
        ClientID = clientID;
    }


    public String Prenom;
    private String imagePath;
    private String Nom;
    private String Adresse;
    private String email;
    private int telephone;
    private List<Commandes> commandes;

    public Client(String prenom, String nom, String adresse, String email, int telephone) {

        this.Prenom = prenom;
        this.Nom = nom;
        this.Adresse = adresse;
        this.email = email;
        this.telephone = telephone;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
    public Client(int clientID, String email) {
        this.ClientID = clientID;
        this.email = email;
    }
    public Client(int clientID,String nom,String prenom,String email,String adresse){
        this.ClientID=clientID;
        this.Nom=nom;
        this.Prenom=prenom;
        this.email=email;
        this.Adresse=adresse;
    }

    public void insertClient(Client client) {
        String sql = "INSERT INTO clients (Prenom, Nom, Email, phone_number, Adresse) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getPrenom());
            statement.setString(2, client.getNom());
            statement.setString(3, client.getEmail());
            statement.setInt(4, client.getTelephone());
            statement.setString(5, client.getAdresse());
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
