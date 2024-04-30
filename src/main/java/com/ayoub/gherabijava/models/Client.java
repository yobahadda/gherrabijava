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
        try {
            TestConnection testConnection=new TestConnection();
            Statement statement =testConnection.getStatement();
            // Corrected SQL query to include all fields and placeholders
            String sql = "INSERT INTO clients (id, First_name, Fast_name, Email, phone_number) VALUES (ClientID,Prenom,Nom,email,telephone)";
//            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultset = statement.executeQuery(sql);
            // Execute the update
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
    public static void main(String agrs[]){
    }

}