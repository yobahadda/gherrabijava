package com.ayoub.gherabijava.models;
import java.sql.*;
import java.util.List;


public class Livraison {
    private int idLivraison;
    private int commandeID;
    private Date dateLivrason;
    private String etat;

    public int getIdLivraison() {
        return idLivraison;
    }

    public void setIdLivraison(int idLivraison) {
        this.idLivraison = idLivraison;
    }

    public int getCommandeID() {
        return commandeID;
    }

    public void setCommandeID(int commandeID) {
        this.commandeID = commandeID;
    }

    public Date getDateLivrason() {
        return dateLivrason;
    }

    public void setDateLivrason(Date dateLivrason) {
        this.dateLivrason = dateLivrason;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
    public void insertLivraison() {
        try {   
            List<Commandes> commande = Commandes.getCommandesByClientId(commandeID);
            if (commande != null) {
                // Insert the delivery details into the database
                String url = "jdbc:mysql://localhost:3306/projetjava";
                String user = "root";
                String password = "";
                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    String sql = "INSERT INTO livraison (CommandeID, DateLivraison, Etat) VALUES (?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, commandeID);
                        statement.setDate(2, new java.sql.Date(getDateLivrason().getTime()));
                        statement.setString(3, etat);
                        int rowsAffected = statement.executeUpdate();
                        System.out.println("Rows affected: " + rowsAffected);
                    }
                }
            } else {
                System.out.println("Order not found for CommandeID: " + commandeID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing delivery
    public void updateLivraison() {
        try {
            String url = "jdbc:mysql://localhost:3306/projetjava";
            String user = "root";
            String password = "";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "UPDATE livraison SET DateLivraison = ?, Etat = ? WHERE idLivraison = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setDate(1, new java.sql.Date(getDateLivrason().getTime()));
                    statement.setString(2, etat);
                    statement.setInt(3, idLivraison);
                    int rowsAffected = statement.executeUpdate();
                    System.out.println("Rows affected: " + rowsAffected);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a delivery
    public void deleteLivraison() {
        try {
            String url = "jdbc:mysql://localhost:3306/projetjava";
            String user = "root";
            String password = "";
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "DELETE FROM livraison WHERE idLivraison = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, idLivraison);
                    int rowsAffected = statement.executeUpdate();
                    System.out.println("Rows affected: " + rowsAffected);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
