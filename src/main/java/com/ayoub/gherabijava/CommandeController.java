package com.ayoub.gherabijava;

import com.ayoub.gherabijava.models.Commandes;
import com.ayoub.gherabijava.models.Produit;
import com.ayoub.gherabijava.models.ProduitFacture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

public class CommandeController implements Initializable {
    final String url = "jdbc:mysql://localhost:3306/projet_java";
    final String password = "S8!hos@samQl";
    final String user = "root";
    private ObservableList<Commandes> commandes = FXCollections.observableArrayList();
    private ObservableList<ProduitFacture> produitCommande = FXCollections.observableArrayList();
    private int clientId;

    @FXML
    private Label pageTitle;

    @FXML
    private TextField nomProduit;

    @FXML
    private Spinner<Integer> spinnerProduitTable;

    @FXML
    private Spinner<Integer> spinnerProduitList;

    @FXML
    private ScrollPane scrollProduits;

    @FXML
    private GridPane gridProduits;

    @FXML
    private TableView<ProduitFacture> produitsTable;

    @FXML
    private TableColumn<ProduitFacture, String> nomProduitColumn;

    @FXML
    private TableColumn<ProduitFacture, String> quantiteColumn;

    @FXML
    private TableColumn<ProduitFacture, String> montantColumn;

    @FXML
    private TableView<Commandes> commandesTable;

    @FXML
    private TableColumn<Commandes,String> statusColumn;

    @FXML
    private TableColumn<Commandes,String> commandeIdColumn;

    @FXML
    private TableColumn<Commandes,LocalDate> dateColumn;

    @FXML
    public void ajouterCommande() {
        System.out.println("ajouterCommande method called");
        Commandes newCommande = new Commandes();
        newCommande.setClientID(clientId);
        newCommande.setDateCommande(Date.valueOf(LocalDate.now()));
        newCommande.setStatut("En Attente");

        // Insert the new command into the database
        int newCommandeId = newCommande.insertCommande(clientId, password);

        // Set the new ID to the command object
        newCommande.setCommandeID(newCommandeId);

        // Add the new command to the observable list
        commandes.add(newCommande);

        // Refresh the commandesTable
        commandesTable.refresh();
    }

    @FXML
    public void supprimerCommande() throws SQLException {
        Commandes selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande != null) {
            selectedCommande.deleteCommande(password);
            commandes.remove(selectedCommande);
            commandesTable.refresh();
        }
    }

    @FXML
    public void confirmProduitTable() {
        ObservableList<ProduitFacture> allProducts = produitsTable.getItems();

        if (!allProducts.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                String sql = "INSERT INTO lignecommande (CommandeID, ID_produit, quantite) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                Commandes selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
                selectedCommande.updateStatus("pret", password);

                // Check if the CommandeID exists in the commandes table
                if (Commandes.getCommandeById(selectedCommande.getCommandeID(), password) != null) {
                    // Iterate over all products and insert each one into the database
                    for (ProduitFacture product : allProducts) {
                        pstmt.setInt(1, selectedCommande.getCommandeID());
                        pstmt.setInt(2, product.getProduitID());
                        pstmt.setInt(3, product.getQuantite());

                        System.out.println("Inserting product with CommandeID: " + selectedCommande.getCommandeID() + ", ID_produit: " + product.getProduitID() + ", quantite: " + product.getQuantite());

                        int rowsAffected = pstmt.executeUpdate();

                        System.out.println("Rows affected: " + rowsAffected);
                    }
                } else {
                    System.out.println("CommandeID does not exist in the commandes table.");
                }

                // Clear the produitsTable
                produitCommande.clear();
                produitsTable.refresh();
                commandesTable.refresh();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No products to confirm.");
        }
    }

    @FXML
    public void supprimerProduit(){
        ProduitFacture selectedProduct = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            produitCommande.remove(selectedProduct);
            produitsTable.refresh();
        }
    }

    @FXML
    public void modifierProduit(){
        ProduitFacture selectedProduct = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int newQuantity = spinnerProduitTable.getValue();
            selectedProduct.setQuantite(newQuantity);
            produitsTable.refresh();
        }
        spinnerProduitTable.getValueFactory().setValue(1);
    }

    @FXML
    public void selectionnerProduitTable(){
        ProduitFacture selectedProduct = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Display the selected product's details in the nomProduit TextField
            nomProduit.setText(selectedProduct.getNom() + " : " + selectedProduct.getMontant() + " DH");
            // Set the value of the spinnerProduitTable to the selected product's quantity
            spinnerProduitTable.getValueFactory().setValue(selectedProduct.getQuantite());
        } else {
            System.out.println("No product selected.");
        }
    }

    @FXML
    private void selectionnerCommande(){
        produitsTable.getItems().clear();
        Commandes selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        getProduitsCommande(selectedCommande.getCommandeID());
        showProduitCommande();
    }

    @FXML
    public void ajouterProduitTable(){
        int quantite = spinnerProduitList.getValue();
        String[] parts = nomProduit.getText().split(" : ");
        String nom = parts[0];
        float prix = Float.parseFloat(parts[1].replace(" DH",""));
        int produitId = getIdByName(nom); // Get the product ID from the product name
        ProduitFacture newProduct = new ProduitFacture(nom, quantite, prix);
        newProduct.setProduitID(produitId); // Set the product ID to the ProduitFacture object
        produitCommande.add(newProduct);
        produitsTable.refresh();
        spinnerProduitList.getValueFactory().setValue(1);
        nomProduit.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        spinnerProduitList.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        spinnerProduitTable.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        String clientEmail = handleDialogBox("Email Du Client");
        showProduits();
        commandesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                produitCommande.clear();
                getProduitsCommande(newSelection.getCommandeID());
                showProduitCommande();
            }
        });
        if(clientEmail != ""){
            getClientId(clientEmail);
            pageTitle.setText("Les Commandes de "+getClientName());
            getCommandesFromDb();
        }
        showCommandes();
    }

    private List<Produit> getAllNewItems() {
        List<Produit> newItems = new ArrayList<>();

        String sql = "SELECT * FROM produit";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Produit item = new Produit();
                // Set the properties of the item based on the columns in the result set
                // This will depend on the columns in your 'produit' table
                item.setIdProduit(rs.getInt("ID_produit"));
                item.setNom(rs.getString("Nom"));
                item.setDescription(rs.getString("Description"));
                item.setPrix(rs.getFloat("PRIX"));
                item.setQuantiteStock(rs.getInt("quantite_stock"));
                item.setImagePath(rs.getString("image_path"));

                newItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newItems;
    }

    public int getIdByName(String productName) {
        int idProduit = -1;
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

    public void workCardData(String nom, float prix){
        nomProduit.setText(nom+" : "+prix+" DH");
    }

    private void showProduitCommande(){
        nomProduitColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

        produitsTable.setItems(produitCommande);
    }

    private void showProduits(){
        ArrayList<Produit> produits = getProduitsFromDb();
        Node card;
        int row = 1;
        int column = 0;
        for (Produit produit : produits) {
            card = setupProduitCard(produit.getNom(),produit.getPrix(),produit.getImagePath());
            gridProduits.add(card, column, row);
            if (column == 1) {
                column = 0;
                row++;
                continue;
            }
            column++;
        }
        scrollProduits.setContent(gridProduits);
    }

    private Node setupProduitCard(String nom, float prix, String imagePath){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("produitCard.fxml"));
        Node card = null;
        try {
            card = loader.load();
            ProduitCard controller = loader.getController();
            controller.setCommandeController(this);
            controller.setCardData(nom,prix,imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return card;
    }

    private ArrayList<Produit> getProduitsFromDb(){
        ArrayList<Produit> produits = new ArrayList<>();
        String nom = "";
        int id = 0;
        String description = "";
        float prix = 0;
        String imagePath = "";
        String sql = "select * from produit";
        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            ResultSet response = statement.executeQuery();
            while(response.next()){
                id = Integer.parseInt(response.getString("ID_produit"));
                nom = response.getString("Nom");
                prix = Float.valueOf(response.getString("PRIX"));
                description = response.getString("Description");
                imagePath = response.getString("image_path");
                produits.add(new Produit(nom,description,prix,imagePath));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return produits;
    }

    private void getProduitsCommande(int commandeId){
        String sql = "select * from lignecommande where CommandeID = ?";
        String nom;
        int quantite;
        int produitId;
        float montant;

        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            statement.setString(1, String.valueOf(commandeId)); // safely set the email parameter
            ResultSet response = statement.executeQuery();
            while(response.next()){
                produitId = Integer.parseInt(response.getString("ID_produit"));
                nom = getNomProduit(produitId);
                quantite = Integer.parseInt(response.getString("quantite"));
                montant = getPrixProduit(produitId) * quantite;
                produitCommande.add(new ProduitFacture(nom,quantite,montant));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private String getNomProduit(int produitId){
        String sql = "select Nom from produit where ID_produit = ?";
        String nom = "";

        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            statement.setString(1, String.valueOf(produitId)); // safely set the email parameter
            ResultSet response = statement.executeQuery();
            if(response.next()){
                nom = response.getString("Nom");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return nom;
    }

    private float getPrixProduit(int produitId){
        String sql = "select PRIX from produit where ID_produit = ?";
        float prix = 0;

        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            statement.setString(1, String.valueOf(produitId)); // safely set the email parameter
            ResultSet response = statement.executeQuery();
            if(response.next()){
                prix = Float.valueOf(response.getString("PRIX"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return prix;
    }

    private void getCommandesFromDb(){
        String sql = "select * from commandes where ClientID = "+clientId;
        String status;
        Date date;
        int commandeId;

        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            ResultSet response = statement.executeQuery();
            while (response.next()) {
                commandeId = Integer.parseInt(response.getString("CommandeID"));
                status = response.getString("Statut");
                date = Date.valueOf(response.getString("DateCommande"));

                commandes.add(new Commandes(commandeId, date, clientId, status));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void showCommandes(){
        commandeIdColumn.setCellValueFactory(new PropertyValueFactory<>("CommandeID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("DateCommande"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));

        commandesTable.setItems(commandes);
    }

    private void getClientId(String email){
        String sql = "select ClientID from clients where Email = ?";
        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            statement.setString(1, email); // safely set the email parameter
            ResultSet response = statement.executeQuery();
            if(response.next()) {
                clientId = Integer.parseInt(response.getString("ClientID"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private String getClientName(){
        String nomComplet = "";
        String sql = "select * from clients where ClientID = ?";
        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            statement.setString(1, String.valueOf(clientId)); // safely set the email parameter
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                String prenom = result.getString("Prenom");
                String nom = result.getString("Nom");
                nomComplet = nom + " "+ prenom;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return nomComplet;
    }

    private String handleDialogBox(String headerText){
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

    private PreparedStatement getDataBaseResponse(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection(url,user,password);
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }
}
