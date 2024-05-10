package com.ayoub.gherabijava.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Facture {
    private int idFacture;
    private int CommandeID;
    private float montantTotal;
    private Date dateFacture;

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public int getCommandeID() {
        return CommandeID;
    }

    public void setCommandeID(int commandeID) {
        CommandeID = commandeID;
    }

    public float getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(float montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Facture() {
    }

    public Facture(int idFacture, int commandeID, float montantTotal, Date dateFacture) {
        this.idFacture = idFacture;
        CommandeID = commandeID;
        this.montantTotal = montantTotal;
        this.dateFacture = dateFacture;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projetjava";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public void calculateMontantTotal() {
        List<Produit> produits = getProductsInCommand(this.CommandeID);
        float total = 0;

        for (Produit produit : produits) {
            total += produit.getPrix();
        }

        this.montantTotal = total;
    }

    public List<Produit> getProductsInCommand(int commandeID) {
        List<Produit> produits = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT p.* FROM produit p JOIN lignecommande lc ON p.ID_produit = lc.ID_produit WHERE lc.CommandeID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, commandeID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Produit produit = new Produit();
                        produit.setID_produit(resultSet.getInt("ID_produit"));
                        produit.setNom(resultSet.getString("Nom"));
                        produit.setDescription(resultSet.getString("Description"));
                        produit.setPrix(resultSet.getDouble("Prix"));
                        produits.add(produit);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }
}
