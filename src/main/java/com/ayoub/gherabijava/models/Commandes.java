package com.ayoub.gherabijava.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Commandes {
    public int CommandeID;
    public Date DateCommande;
    public int ClientID;
    public String Statut;

    public Commandes(int commandeID, Date dateCommande, int clientID, String statut) {
        CommandeID = commandeID;
        DateCommande = dateCommande;
        ClientID = clientID;
        Statut = statut;
    }

    public int getCommandeID() {
        return CommandeID;
    }

    public void setCommandeID(int commandeID) {
        CommandeID = commandeID;
    }

    public Date getDateCommande() {
        return DateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        DateCommande = dateCommande;
    }

    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int clientID) {
        ClientID = clientID;
    }

    public String getStatut() {
        return Statut;
    }

    public void setStatut(String statut) {
        Statut = statut;
    }

    @Override
    public String toString() {
        return "Commandes{" +
                "CommandeID=" + CommandeID +
                ", DateCommande=" + DateCommande +
                ", ClientID=" + ClientID +
                ", Statut='" + Statut + '\'' +
                '}';
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projetjava";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public static List<Commandes> getCommandesByClientId(int clientId) {
        List<Commandes> commandesList = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM commandes WHERE ClientID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, clientId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int commandeID = resultSet.getInt("CommandeID");
                    Date dateCommande = resultSet.getDate("DateCommande");
                    int clientIDFromResult = resultSet.getInt("ClientID");
                    String statut = resultSet.getString("Statut");
                    Commandes commande = new Commandes(commandeID, dateCommande, clientIDFromResult, statut);
                    commandesList.add(commande);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandesList;
    }

    public void insertCommande() {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO commandes (DateCommande, ClientID, Statut) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, this.getDateCommande());
                statement.setInt(2, this.getClientID());
                statement.setString(3, this.getStatut());
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCommande() {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM commandes WHERE CommandeID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, this.getCommandeID());
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Commandes> result = getCommandesByClientId(2);
        for (Commandes commande : result) {
            System.out.println(commande);
        }
    }
}

