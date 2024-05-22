package com.ayoub.gherabijava;

import com.ayoub.gherabijava.models.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    final String url = "jdbc:mysql://localhost:3306/projet_java";
    private File selectedFile;
    final String password = "";
    final String user = "root";
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private FilteredList<Client> filteredClients;
    @FXML
    private TextField prenomField, nomField, emailField, teleField, adresseField, searchField;
    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, String> prenomColumn, nomColumn, emailColumn, telephoneColumn, adresseColumn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clients = getClients();  // Load initial data
        filteredClients = new FilteredList<>(clients, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterClients(newValue));
        showClient();

        // Add listener to client selection
        clientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateImageView(newSelection);
            }
        });
    }

    public void showClient() {
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        clientsTable.setItems(filteredClients);
    }
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }
    public void insertClient(Client client, File imageFile) {
        // Prepare the SQL query to insert data into both image-related columns
        String sql = "INSERT INTO clients (Prenom, Nom, Email, telephone, Adresse, Client_image, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Using try-with-resources to ensure all resources are closed properly
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             FileInputStream inputStream = new FileInputStream(imageFile)) {  // Input stream for the image

            // Set parameters for the PreparedStatement
            statement.setString(1, client.getPrenom());
            statement.setString(2, client.getNom());
            statement.setString(3, client.getEmail());
            statement.setInt(4, client.getTelephone());
            statement.setString(5, client.getAdresse());
            statement.setBlob(6, inputStream);
            statement.setString(7, imageFile.getAbsolutePath());

            // Execute the update and check the result
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insertion successful with image and image path. Rows affected: " + rowsAffected);
            } else {
                System.out.println("No rows affected. Check your SQL statement and data integrity.");
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error during insertion: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void ajouterClient() {
        Client newClient = getNewClient();
        if (selectedFile != null) {
            insertClient(newClient, selectedFile);
            selectedFile = null; // Clear the selected file after use
        } else {
            System.out.println("No image selected");
        }
        refreshClientList(); // Refresh the client list from database
        clearFields();
        showClient(); // Refresh TableView
    }
    private void refreshClientList() {
        clients.clear(); // Clear the current list
        clients.addAll(getClients()); // Re-fetch the clients
    }


    @FXML
    private ImageView clientImageView;
    private void updateImageView(Client client) {
        if (client.getImagePath() != null && !client.getImagePath().isEmpty()) {
            try {
                File imgFile = new File(client.getImagePath());
                if(imgFile.exists()) {
                    Image image = new Image(imgFile.toURI().toString());
                    clientImageView.setImage(image);
                } else {
                    System.out.println("Image file does not exist: " + client.getImagePath());
                    clientImageView.setImage(null);
                }
            } catch (Exception e) {
                System.out.println("Cannot load image: " + e.getMessage());
                clientImageView.setImage(null);
            }
        } else {
            clientImageView.setImage(null);
        }
    }





    @FXML
    public void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            clientImageView.setImage(image);
            selectedFile = file; // This needs to be declared at the class level
        }
    }

    @FXML
    public void clearFields(){
        prenomField.setText("");
        nomField.setText("");
        emailField.setText("");
        teleField.setText("");
        adresseField.setText("");
    }

    private boolean deleteClientFromDB(Client client) {
        String sql = "DELETE FROM clients WHERE Email = ?";  // Assuming Email is unique; replace with ID if available
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getEmail());  // Set the identifier for deletion
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error during deletion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    @FXML
    public void supprimerClient() {
        Client deletedClient = getFieldsValues();
        if (deletedClient != null && deleteClientFromDB(deletedClient)) {
            clients.remove(deletedClient);
            System.out.println("Client successfully deleted from both the GUI and database.");
        } else {
            System.out.println("Failed to delete client from the database.");
        }
        showClient();
    }

    @FXML
    public void modifierClient() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            // Update client details from fields
            updateClientDetails(selectedClient);

            // Update the client in the database
            updateClientInDB(selectedClient);

            // Refresh the table to show updated details
            clientsTable.refresh();
        } else {
            System.out.println("No client selected for modification.");
        }
    }
    private void updateClientInDB(Client client) {
        String sql = "UPDATE clients SET Prenom = ?, Nom = ?, Email = ?, telephone = ?, Adresse = ?, image_path = ? WHERE Email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getPrenom());
            statement.setString(2, client.getNom());
            statement.setString(3, client.getEmail());
            statement.setInt(4, client.getTelephone());
            statement.setString(5, client.getAdresse());
            statement.setString(6, client.getImagePath());  // Assuming image path is not changed here
            statement.setString(7, client.getEmail());  // Assuming you have a getClientID() method

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating client: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void updateClientDetails(Client client) {
        client.setPrenom(prenomField.getText());
        client.setNom(nomField.getText());
        client.setEmail(emailField.getText());
        client.setAdresse(adresseField.getText());
        client.setTelephone(Integer.parseInt(teleField.getText()));
    }
    private Client getFieldsValues(){
        String prenom = prenomField.getText();
        String nom = nomField.getText();
        String email = emailField.getText();
        String adresse = adresseField.getText();
        int telephone = Integer.parseInt(teleField.getText());
        return new Client(prenom,nom,adresse,email,telephone);
    }

    @FXML
    private void selectionnerClient(){
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        prenomField.setText(selectedClient.getPrenom());
        nomField.setText(selectedClient.getNom());
        emailField.setText(selectedClient.getEmail());
        adresseField.setText(selectedClient.getAdresse());
        teleField.setText(String.valueOf(selectedClient.getTelephone()));
    }

    private Client getNewClient(){
        String prenom = prenomField.getText();
        String nom = nomField.getText();
        String email = emailField.getText();
        String adresse = adresseField.getText();
        int telephone = Integer.parseInt(teleField.getText());

        return new Client(prenom, nom, adresse, email, telephone);
    }
    private void filterClients(String searchText) {
        filteredClients.setPredicate(client -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lowerCaseFilter = searchText.toLowerCase();
            if (client.getPrenom().toLowerCase().contains(lowerCaseFilter)) return true;
            if (client.getNom().toLowerCase().contains(lowerCaseFilter)) return true;
            if (client.getEmail().toLowerCase().contains(lowerCaseFilter)) return true;
            return false;  // Does not match
        });
        clientsTable.setItems(filteredClients);  // Set the filtered items to the table
    }

    private ObservableList<Client> getClients() {
        ObservableList<Client> clientList = FXCollections.observableArrayList();
        String sql = "SELECT Prenom, Nom, Adresse, Email, Telephone, image_path FROM clients";

        try (Connection connection = getConnection(); // Assuming getConnection() handles your database connection setup
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("Prenom"),
                        resultSet.getString("Nom"),
                        resultSet.getString("Adresse"),
                        resultSet.getString("Email"),
                        resultSet.getInt("Telephone")
                );
                client.setImagePath(resultSet.getString("image_path"));
                clientList.add(client);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving clients from the database: " + e.getMessage());
            e.printStackTrace();
        }
        return clientList;
    }
}
