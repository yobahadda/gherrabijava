package com.ayoub.gherabijava.models;

import java.sql.*;

public class Client {
    private int ClientID;
    public String Prenom;
    private String Nom;
    private String Adresse;
    private String email;
    private String telephone;



    public Client(int clientID, String prenom, String nom, String adresse, String email, String telephone) {
        this.ClientID = clientID;
        this.Prenom = prenom;
        this.Nom = nom;
        this.Adresse = adresse;
        this.email = email;
        this.telephone = telephone;
    }
    public void insertClient() {
        String url = "jdbc:mysql://localhost:3306/projetjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Corrected SQL query to include all fields and placeholders
            String sql = "INSERT INTO clients (id, First_name, Fast_name, Email, phone_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameters in the same order as they appear in the SQL query
            statement.setInt(1, ClientID);
            statement.setString(2, Prenom);
            statement.setString(3, Nom);
            statement.setString(4, email);
            statement.setString(5, telephone);
            // Execute the update
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteClient() {
        String url = "jdbc:mysql://localhost:3306/projetjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM clients WHERE ClientID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, ClientID);

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}