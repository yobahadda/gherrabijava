package com.ayoub.gherabijava;

import com.ayoub.gherabijava.models.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    final String url = "jdbc:mysql://localhost:3306/projet_java";
    final String password = "S8!hos@samQl";
    final String user = "root";
    private ObservableList<Client> clients = FXCollections.observableArrayList();

    @FXML
    private TextField prenomField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField teleField;

    @FXML
    private TextField adresseField;

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, String> prenomColumn;

    @FXML
    private TableColumn<Client, String> nomColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> telephoneColumn;

    @FXML
    private TableColumn<Client, String> adresseColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showClient();
    }

    public void showClient(){
        ObservableList<Client> clients = getClients();

        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        clientsTable.setItems(clients);
    }

    @FXML
    public void ajouterClient(){
        clients.add(getNewClient());
    }

    @FXML
    public void supprimerClient(){
        Client deletedClient = selectionnerClient();
        clients.remove(deletedClient);
    }

    @FXML
    public void modifierClient(){
        Client modifiedClient = selectionnerClient();
        clients.remove(modifiedClient);
        clients.add(getNewClient());
    }

    private Client selectionnerClient(){
        return clientsTable.getSelectionModel().getSelectedItem();
    }

    private Client getNewClient(){
        String prenom = prenomField.getText();
        String nom = nomField.getText();
        String email = emailField.getText();
        String adresse = adresseField.getText();
        int telephone = Integer.parseInt(teleField.getText());

        return new Client(prenom, nom, adresse, email, telephone);
    }

    private ObservableList<Client> getClients(){
        String sql = "select * from clients";
        String prenom;
        String nom;
        String email;
        String adresse;
        int telephone;

        try{
            ResultSet response = getDataBaseResponse(sql);
            while (response.next()){
                prenom = response.getString("Prenom");
                nom = response.getString("Nom");
                email = response.getString("Email");
                adresse = response.getString("Adresse");
                telephone = response.getInt("Telephone");

                Client newClient = new Client(prenom,nom,adresse,email,telephone);
                clients.add(newClient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    private ResultSet getDataBaseResponse(String sql) throws SQLException{
        Connection connection = DriverManager.getConnection(url,user,password);
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        return result;
    }
}
