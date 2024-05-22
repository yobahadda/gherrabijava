package com.ayoub.gherabijava.models;

import java.sql.*;

public class Produit {
    private int idProduit;
    private String nom;
    private String description;
    private Float prix;
    private Float poids;

    private Integer quantiteStock;
    private String imagePath;
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }

    // Constructors
    public Produit() {
    }

    public Produit(String nom, String description, Float prix, Float poids, Integer quantiteStock) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.poids = poids;
        this.quantiteStock = quantiteStock;
    }
    public Produit(String nom, String description, Float prix) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }
    public Produit(String nom, String description, Float prix,String imagePath) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.imagePath= imagePath;
    }

    // Getters and setters
    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }
    public Float getPoids() {
        return poids;
    }
    public void setPoids(Float poids) {
        this.poids = poids;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }
    public static Produit getProductById(int idProduit) {
        Produit produit = null;
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";

        String sql = "SELECT * FROM produit WHERE ID_produit = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idProduit);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String description = resultSet.getString("Description");
                Float prix = resultSet.getFloat("PRIX");
                Float poids = resultSet.getFloat("Poids");
                Integer quantiteStock = resultSet.getInt("quantite_stock");
                String imagePath = resultSet.getString("image_path");

                produit = new Produit(nom, description, prix, poids, quantiteStock);
                produit.setIdProduit(idProduit);
                produit.setImagePath(imagePath);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produit;
    }
    public static int getIdByName(String productName) {
        int idProduit = -1;
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";

        String sql = "SELECT ID_produit FROM produit WHERE Nom = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                idProduit = resultSet.getInt("ID_produit");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idProduit;
    }
    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }
}
