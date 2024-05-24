package com.ayoub.gherabijava;

import com.ayoub.gherabijava.models.Client;
import com.ayoub.gherabijava.models.Commandes;
import com.ayoub.gherabijava.models.Facture;
import com.ayoub.gherabijava.models.ProduitFacture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class FactureController implements Initializable {
    final String url = "jdbc:mysql://localhost:3306/projet_java";
    final String password = "S8!hos@samQl";
    final String user = "root";
    private int clientId;
    private Client selectedClient;
    private ObservableList<ProduitFacture> produitFactures = FXCollections.observableArrayList();
    private ObservableList<Facture> factures = FXCollections.observableArrayList();
    private ObservableList<Commandes> commandes = FXCollections.observableArrayList();

    @FXML
    private Label pageTitle;

    @FXML
    private TableView<Facture> factureTable;

    @FXML
    private TableColumn<Facture, Integer> comFactureColumn;

    @FXML
    private TableColumn<Facture, Float> totalColumn;

    @FXML
    private TableColumn<Facture, Float> poidsColumn;

    @FXML
    private TableColumn<Facture, Date> dateFactureColumn;

    @FXML
    private TableView<ProduitFacture> produitFactureTable;

    @FXML
    private TableColumn<ProduitFacture, String> nomColumn;

    @FXML
    private TableColumn<ProduitFacture, Integer> quantiteColumn;

    @FXML
    private TableColumn<ProduitFacture, Float> montantColumn;

    @FXML
    private TableView<Commandes> commandesTable;

    @FXML
    private TableColumn<Commandes, Integer> comIdColumn;

    @FXML
    private TableColumn<Commandes, String> statusColumn;

    @FXML
    private TableColumn<Commandes, Date> dateColumn;

    @FXML
    private TextArea bonneDeLivraisonTextArea;

    @FXML
    public void generateBonneDeLivraison() throws SQLException {
        Facture selectedFacture = selectionnerFacture();

        if (selectedFacture == null) {
            showAlert("No Facture Selected", "Please select a facture to generate a bonne de livraison.");
            return;
        }

        Commandes selectedCommande = Commandes.getCommandeById(selectedFacture.getCommandeID(),password);
        Client client = getClientById(selectedCommande.getClientID());

        // Calculate total weight
        float totalWeight = produitFactures.stream().map(p -> p.getPoids() * p.getQuantite()).reduce(0.0f, Float::sum);

        // Generate the "bonne de livraison"
        StringBuilder bonneDeLivraison = new StringBuilder();
        bonneDeLivraison.append("Bonne de livraison for Facture ID: ").append(selectedFacture.getFactureID()).append("\n");
        bonneDeLivraison.append("Commande ID: ").append(selectedFacture.getCommandeID()).append("\n");
        bonneDeLivraison.append("Montant Total: ").append(selectedFacture.getMontantTotal()).append("\n");
        bonneDeLivraison.append("Total Weight: ").append(totalWeight).append(" kg\n");
        bonneDeLivraison.append("Date Facture: ").append(selectedFacture.getDateFacture()).append("\n\n");

        // Add client details
        bonneDeLivraison.append("Client Details:\n");
        bonneDeLivraison.append("Nom: ").append(client.getNom()).append(" ").append(client.getPrenom()).append("\n");
        bonneDeLivraison.append("Email: ").append(client.getEmail()).append("\n");
        bonneDeLivraison.append("Adresse: ").append(client.getAdresse()).append("\n\n");

        // Add product details
        bonneDeLivraison.append("Product Details:\n");
        for (ProduitFacture produit : produitFactures) {
            bonneDeLivraison.append("Produit: ").append(produit.getNom()).append("\n");
            bonneDeLivraison.append("Quantite: ").append(produit.getQuantite()).append("\n");
            bonneDeLivraison.append("Montant: ").append(produit.getMontant()).append("\n");
            bonneDeLivraison.append("Weight: ").append(produit.getPoids()).append(" kg\n\n");
        }

        // Update the UI to display the "bonne de livraison"
        bonneDeLivraisonTextArea.setText(bonneDeLivraison.toString());
        Facture newFacture = new Facture(selectedFacture.getCommandeID(), selectedFacture.getMontantTotal(), totalWeight, Date.valueOf(LocalDate.now()));

        // Insert the new Facture into the database
        insertFacture(newFacture);
    }

    @FXML
    public Facture selectionnerFacture() {
        return factureTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void generatePdfFile() {
        Facture selectedFacture = selectionnerFacture();
        if (selectedFacture != null) {
            System.out.println(selectedFacture.getMontantTotal());
        } else {
            showAlert("No Facture Selected", "Please select a facture to generate the PDF file.");
        }
    }

    @FXML
    private void selectionnerCommande() {
        Commandes selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            int commandeId = selectedCommande.getCommandeID();
            getProduitsCommande(commandeId);
            showProduitFactures();
            showFacture(commandeId);
        } else {
            System.out.println("No item selected in commandesTable");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String clientEmail = handleDialogBox("Email Du Client");

        selectedClient = getClientByEmail(clientEmail);
        if (selectedClient == null) {
            showAlert("Client Not Found", "Client not found with email: " + clientEmail);
            return;
        }

        pageTitle.setText("Les Factures De " + selectedClient.getNom());

        commandesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                produitFactures.clear();
                factures.clear();
                getProduitsCommande(newSelection.getCommandeID());
                showProduitFactures();
                showFacture(newSelection.getCommandeID());
            }
        });

        getCommandesFromDb();
        showCommandes();
        setupFactureTable();

    }

    public void insertFacture(Facture facture) {
        String sql = "INSERT INTO factures (commandeId, montantTotal, dateFacture, poidsTotal) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, facture.getCommandeID());
            statement.setFloat(2, facture.getMontantTotal());
            statement.setDate(3, facture.getDateFacture());
            statement.setFloat(4, facture.getPoidsTotal());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupFactureTable() {
        comFactureColumn.setCellValueFactory(new PropertyValueFactory<>("commandeID"));
        dateFactureColumn.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        factureTable.setItems(factures);
    }

    private Client getClientByEmail(String email) {
        String query = "SELECT * FROM clients WHERE Email = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("ClientID"), rs.getString("Prenom"), rs.getString("Nom"), rs.getString("Email"), rs.getString("Adresse"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Client getClientById(int clientId) {
        String query = "SELECT * FROM clients WHERE ClientID = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("ClientID"), rs.getString("Prenom"), rs.getString("Nom"), rs.getString("Email"), rs.getString("Adresse"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void updateProduitsTable(int commandeId) {
        produitFactureTable.getItems().clear();
        String sql = "SELECT * FROM lignecommande WHERE CommandeID = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, commandeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int produitId = resultSet.getInt("ID_produit");
                int quantite = resultSet.getInt("quantite");
                String nomProduit = getNomProduit(produitId);
                float prixProduit = getPrixProduit(produitId);
                float poidsProduit = getPoidsProduit(produitId);
                float montant = prixProduit * quantite;
                produitFactureTable.getItems().add(new ProduitFacture(nomProduit, quantite, montant, poidsProduit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getCommandesFromDb() {
        String sql = "SELECT * FROM commandes WHERE ClientID = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, selectedClient.getClientID());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                commandes.add(new Commandes(resultSet.getInt("CommandeID"),
                        Date.valueOf(resultSet.getDate("DateCommande").toLocalDate()),
                        resultSet.getString("Statut")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getProduitsCommande(int commandeId) {
        produitFactures.clear();
        factures.clear();
        String sql = "SELECT * FROM lignecommande WHERE CommandeID = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, commandeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                produitFactures.add(new ProduitFacture(
                        getNomProduit(resultSet.getInt("ID_produit")),
                        resultSet.getInt("quantite"),
                        resultSet.getFloat("quantite") * getPrixProduit(resultSet.getInt("ID_produit")),
                        getPoidsProduit(resultSet.getInt("ID_produit"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String handleDialogBox(String headerText) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Email du Client");
        textInputDialog.setHeaderText(headerText);
        textInputDialog.getDialogPane().setContentText("Entrez L'Email Du Client:");
        Optional<String> response = textInputDialog.showAndWait();
        String input = "";
        if(response.isPresent()) {
            input = response.get();
            if (input.trim().isEmpty()) {
                input = handleDialogBox("invalid input value");
            }
        }
        return input.trim();
    }

    private void getFacture(int commandeId) {
        Date dateFacture = Date.valueOf(LocalDate.now());
        float montantTotal = produitFactures.stream().map(ProduitFacture::getMontant).reduce(0.0f, Float::sum);
        factures.add(new Facture(commandeId, montantTotal, dateFacture));
    }

    private void showFacture(int commandeId) {
        getFacture(commandeId);
        comFactureColumn.setCellValueFactory(new PropertyValueFactory<>("commandeID"));
        dateFactureColumn.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        poidsColumn.setCellValueFactory(new PropertyValueFactory<>("poidsTotal"));
        factureTable.setItems(factures);
    }

    private void showCommandes() {
        comIdColumn.setCellValueFactory(new PropertyValueFactory<>("commandeID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        commandesTable.setItems(commandes);
    }

    private void showProduitFactures() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        produitFactureTable.setItems(produitFactures);
    }

    private String getNomProduit(int produitId) {
        String sql = "SELECT Nom FROM produit WHERE ID_produit = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, produitId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("Nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private float getPrixProduit(int produitId) {
        String sql = "SELECT PRIX FROM produit WHERE ID_produit = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, produitId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getFloat("PRIX");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    private float getPoidsProduit(int produitId) {
        String sql = "SELECT poids FROM produit WHERE ID_produit = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, produitId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getFloat("poids");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private PreparedStatement getDataBaseResponse(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection.prepareStatement(sql);
    }
}