package com.ayoub.gherabijava.models;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class Lignecommande {
    private int idLigneCommande;
    private int CommandeID;
    private int ID_produit;
    private Produit produit; // Assuming you have a Produit class
    private int quantite;
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
    public int getCommandeID() {
        return CommandeID;
    }
    public void setCommandeID(int commandeID) {
        CommandeID = commandeID;
    }
    public int getIdLigneCommande() {
        return idLigneCommande;
    }
    public void setIdLigneCommande(int idLigneCommande) {
        this.idLigneCommande = idLigneCommande;
    }
    public int getID_produit() {
        return ID_produit;
    }
    public void setID_produit(int ID_produit) {
        this.ID_produit = ID_produit;
    }
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public void insert() {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO lignecommande (CommandeID, ID_produit, quantite) VALUES (?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, CommandeID);
                statement.setInt(2, ID_produit);
                statement.setInt(3, quantite);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE lignecommande SET quantite = ? WHERE idLigneCommande = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, quantite);
                statement.setInt(2, idLigneCommande);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM lignecommande WHERE idLigneCommande = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idLigneCommande);
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Lignecommande> getAll() {
        List<Lignecommande> lignes = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM lignecommande";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Lignecommande ligne = new Lignecommande();
                    ligne.setIdLigneCommande(resultSet.getInt("idLigneCommande"));
                    ligne.setCommandeID(resultSet.getInt("CommandeID"));
                    ligne.setID_produit(resultSet.getInt("ID_produit"));
                    ligne.setQuantite(resultSet.getInt("quantite"));
                    lignes.add(ligne);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lignes;
    }
    public Produit getProduit() {
        return produit;
    }

    public int getQuantity() {
        return quantite;
    }
}