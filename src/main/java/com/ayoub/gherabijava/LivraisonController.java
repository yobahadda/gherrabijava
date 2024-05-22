package com.ayoub.gherabijava;

import com.ayoub.gherabijava.models.Camion;
import com.ayoub.gherabijava.models.Commandes;
import com.ayoub.gherabijava.models.Facture;
import com.ayoub.gherabijava.models.Produit;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class LivraisonController implements Initializable {
    final String url = "jdbc:mysql://localhost:3306/projet_java";
    final String password = "";
    final String user = "root";
    private ObservableList<Facture> factures = FXCollections.observableArrayList();
    private ArrayList<Integer> commandesId = new ArrayList<>();
    private ArrayList<Camion> camions = new ArrayList<>();
    private int clientId;
    private int selectedCoId;
    private int selectedCaId;

    @FXML
    private Label pageTitle;

    @FXML
    private Label camionAdresse;

    @FXML
    private ScrollPane scrollCamion;

    @FXML
    private GridPane gridCamion;

    @FXML
    private TableView<Facture> factureTable;

    @FXML
    private TableColumn<Facture, String> coIdColumn;

    @FXML
    private TableColumn<Facture, String> montantTotalColumn;

    @FXML
    private TableColumn<Facture, String> poidsTotalColumn;

    @FXML
    private TableColumn<Facture, String> dateFactureColumn;

    @FXML
    private void selectionnerLivraison(){
        Facture selectedFacture = factureTable.getSelectionModel().getSelectedItem();
        selectedCoId = selectedFacture.getCommandeID();
    }

    @FXML
    private void confirmLivraison(){
        String sql = "insert into livraisons (commandeID, dateLivraison, etatLivraison, camion_id) values (?,?,?,?)";
        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            statement.setInt(1, selectedCoId);
            statement.setDate(2, new Date(System.currentTimeMillis()));
            statement.setString(3, "livré");
            statement.setInt(4, selectedCaId);
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String email = handleDialogBox("Entrez L'Email Du Client");
        showCamions();
        if(email != ""){
            getClientId(email);
            pageTitle.setText("Les Livraisons de "+getClientName());
            getCommandesFromDb();
            getFacturesFromDb();
        }
        showFactures();
    }

    private void showFactures(){
        coIdColumn.setCellValueFactory(new PropertyValueFactory<>("CommandeID"));
        montantTotalColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        poidsTotalColumn.setCellValueFactory(new PropertyValueFactory<>("poidsTotal"));
        dateFactureColumn.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));

        factureTable.setItems(factures);
    }

    private void showCamions(){
        getCamionsFromDb();
        Node card;
        int row = 1;
        int column = 0;
        for (Camion camion : camions) {
            card = setupCamionCard(camion.getAdresse(),camion.getPoidsTotal(),camion.getPoidsCourant(),camion.getId());
            gridCamion.add(card, column, row);
            if (column == 1) {
                column = 0;
                row++;
                continue;
            }
            column++;
        }
        scrollCamion.setContent(gridCamion);
    }

    public void workCardData(String adresse, int camionId){
        camionAdresse.setText(adresse);
        selectedCaId = camionId;
    }

    private void getCamionsFromDb(){
        String sql = "select * from camion";
        int id;
        String matricule;
        float poidsTotal;
        String adresse;
        float poidsCourant;

        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            ResultSet response = statement.executeQuery();
            while (response.next()) {
                id = Integer.parseInt(response.getString("id"));
                matricule = response.getString("matricule");
                poidsTotal = Float.valueOf(response.getString("poids_total"));
                adresse = response.getString("adresse");
                poidsCourant = Float.valueOf(response.getString("poids_courant"));
                camions.add(new Camion(id,matricule, adresse, poidsTotal, poidsCourant));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Node setupCamionCard(String adresse, float poidsTotal, float poidsCourant, int camionId){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("camionCard.fxml"));
        Node card = null;
        try {
            card = loader.load();
            CamionCard controller = loader.getController();
            controller.setCamionController(this);
            controller.setCardData(adresse, poidsTotal, poidsCourant, camionId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return card;
    }

    private void getCommandesFromDb(){
        String sql = "select * from commandes where Statut = 'ready' and ClientID = " + clientId;
        int commandeId;
        try{
            PreparedStatement statement = getDataBaseResponse(sql);
            ResultSet response = statement.executeQuery();
            while (response.next()) {
                commandeId = Integer.parseInt(response.getString("CommandeID"));
                commandesId.add(commandeId);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void getFacturesFromDb(){
        String baseSql = "select * from factures where CommandeID = ";
        float montantTotal;
        float poidsTotal;
        Date date;
        for(int commandeId : commandesId){
            String sql = baseSql + commandeId;
            try{
                PreparedStatement statement = getDataBaseResponse(sql);
                ResultSet response = statement.executeQuery();
                while (response.next()) {
                    montantTotal = Float.valueOf(response.getString("montantTotal"));
                    date = Date.valueOf(response.getString("DateFacture"));
                    poidsTotal = Float.valueOf(response.getString("PoidsTotal"));
                    factures.add(new Facture(commandeId, montantTotal, poidsTotal, date));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
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
        System.out.println("Client ID: "+clientId);
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

    private PreparedStatement getDataBaseResponse(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection(url,user,password);
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
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

}
