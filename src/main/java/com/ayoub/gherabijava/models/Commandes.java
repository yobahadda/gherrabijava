package com.ayoub.gherabijava.models;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
public class Commandes {
    public int commandeID;
    private Date dateCommande;
    private int clientID;
    private String statut;
    private List<Lignecommande> ligneCommandes;
    public float getTotalWeight() {
        float totalWeight = 0.0f;
        for (Lignecommande ligne : ligneCommandes) {
            totalWeight += ligne.getProduit().getPoids() * ligne.getQuantity();
        }
        return totalWeight;
    }
    public Commandes(Date dateCommande, int clientID, String statut) {
        this.dateCommande = dateCommande;
        this.clientID = clientID;
        this.statut = statut;
    }
    public Commandes(int commandeID,Date dateCommande, int clientID, String statut) {
        this.commandeID = commandeID;
        this.dateCommande = dateCommande;
        this.clientID = clientID;
        this.statut = statut;
    }
    public Commandes(int commandeID,Date dateCommande,  String statut) {
        this.commandeID = commandeID;
        this.dateCommande = dateCommande;
        this.statut = statut;
    }
    public void updateStatus(String newStatus) {
        String sql = "UPDATE commandes SET Statut = ? WHERE CommandeID = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setInt(2, this.commandeID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Commandes() {

    }

    public int getCommandeID() {
        return commandeID;
    }

    public void setCommandeID(int commandeID) {
        this.commandeID = commandeID;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public static Commandes getCommandeById(int commandeId) throws SQLException {
        Commandes commande = null;
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM commandes WHERE CommandeID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, commandeId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Date dateCommande = resultSet.getDate("DateCommande");
                    int clientID = resultSet.getInt("ClientID");
                    String statut = resultSet.getString("Statut");
                    commande = new Commandes(dateCommande, clientID, statut);
                    commande.setCommandeID(commandeId);
                }
            }
        }
        return commande;
    }
    public int insertCommande(int clientId) {
        String insertCommandeSql = "INSERT INTO commandes (ClientID, DateCommande) VALUES (?, ?)";
        int generatedCommandeId = 0;
        try (Connection connection = getConnection();
             PreparedStatement insertCommandeStmt = connection.prepareStatement(insertCommandeSql, Statement.RETURN_GENERATED_KEYS)) {
            // Set the ClientID and DateCommande in the prepared statement
            insertCommandeStmt.setInt(1, clientId);
            insertCommandeStmt.setDate(2, this.getDateCommande());

            int rowsAffected = insertCommandeStmt.executeUpdate();

            System.out.println("Rows affected by insertCommande: " + rowsAffected);

            if (rowsAffected > 0) {
                try (ResultSet rs = insertCommandeStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedCommandeId = rs.getInt(1);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedCommandeId;
    }

    public void deleteCommande() throws SQLException {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM commandes WHERE CommandeID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, this.commandeID);
                statement.executeUpdate();
            }
        }
    }
}
