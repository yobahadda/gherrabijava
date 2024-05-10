package com.ayoub.gherabijava.models;
import java.sql.*;
public class Produit {
    private int ID_produit;
    private String Nom;
    private String Description;
    private Double Prix;
    public Produit(int ID_produit, String nom, String description, Double prix) {
        this.ID_produit = ID_produit;
        Nom = nom;
        Description = description;
        Prix = prix;
    }

    public Produit() {

    }

    public int getID_produit() {
        return ID_produit;
    }
    public void setID_produit(int ID_produit) {
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
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projetjava";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public void deleteProduct() {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM produit WHERE ID_produit = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, this.ID_produit);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduct() {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO produit (ID_produit, Nom, Description, PRIX) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, this.ID_produit);
                statement.setString(2, this.Nom);
                statement.setString(3, this.Description);
                statement.setDouble(4, this.Prix);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyProduct(String newNom, String newDescription, Double newPrix) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE produit SET Nom = ?, Description = ?, PRIX = ? WHERE ID_produit = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newNom);
                statement.setString(2, newDescription);
                statement.setDouble(3, newPrix);
                statement.setInt(4, this.ID_produit);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Product modified successfully!");
                } else {
                    System.out.println("No product found with the given ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Produit telephone = new Produit(2, "SAMSUNG S22", "NEUF", 1300.0);
        Produit laptop = new Produit(2, "APPLE MacBook Pro", "NEUF", 2500.0);
        laptop.addProduct();
        System.out.println("Laptop added successfully!");

        Produit tablet = new Produit(3, "SAMSUNG Galaxy Tab", "NEUF", 500.0);
        tablet.addProduct();
        System.out.println("Tablet added successfully!");
    }
}