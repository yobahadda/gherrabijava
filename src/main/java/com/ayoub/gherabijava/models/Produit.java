package com.ayoub.gherabijava.models;
import java.sql.*;
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
    public void addProduct() {
        String url = "jdbc:mysql://localhost:3306/yourDatabase?useSSL=false&serverTimezone=UTC";
        String user = "yourUsername";
        String password = "yourPassword";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO produit (ID_produit, Nom, Description, Prix) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, ID_produit);
            statement.setString(2, Nom);
            statement.setString(3, Description);
            statement.setDouble(4, Prix);

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
