package com.ayoub.gherabijava.models;

import java.sql.*;
import java.util.List;

public class Client {
    private int ClientID;
    public String Prenom;
    private String Nom;
    private String Adresse;
    private String email;
    private String telephone;
    private List<Commandes> commandes;

    public Client(int clientID, String prenom, String nom, String adresse, String email, String telephone) {
        this.ClientID = clientID;
        this.Prenom = prenom;
        this.Nom = nom;
        this.Adresse = adresse;
        this.email = email;
        this.telephone = telephone;
    }

    public int getClientID() {
        return this.ClientID;
    }

    public void setClientID(int clientID) {
        ClientID = clientID;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projetjava";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public void insertClient() {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO clients (ClientID, Prenom, Nom, Email, phone_number) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, this.getClientID());
                statement.setString(2, this.getPrenom());
                statement.setString(3, this.getNom());
                statement.setString(4, this.getEmail());
                statement.setString(5, this.getTelephone());
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient() {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM clients WHERE ClientID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, this.getClientID());
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
