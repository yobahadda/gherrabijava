package com.ayoub.gherabijava;

import com.ayoub.gherabijava.models.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class ProduitController implements Initializable {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/projet_java";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    @FXML
    private TextField searchField;
    @FXML private ImageView imagePreview;
    @FXML private TextField nomField, prixField, poidsField, quantiteField;
    @FXML private TextField descField;
    @FXML private TableView<Produit> produitsTable;
    @FXML private TableColumn<Produit, String> nomColumn, descColumn;
    @FXML private TableColumn<Produit, Double> prixColumn, poidsColumn;
    @FXML private TableColumn<Produit, Integer> quantiteColumn;
    @FXML private ImageView productImageView;
    private ObservableList<Produit> produits = FXCollections.observableArrayList();
    private FilteredList<Produit> filteredProducts;

    private File selectedImageFile;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableColumns();
        loadProduits();
        setupSearchFilter();
        produitsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateImageView(newSelection);
                populateFields(newSelection);  // Populate the fields when a new item is selected
            }
        });
    }
    private void populateFields(Produit selectedProduct) {
        nomField.setText(selectedProduct.getNom());
        descField.setText(selectedProduct.getDescription());
        prixField.setText(String.valueOf(selectedProduct.getPrix()));
        poidsField.setText(String.valueOf(selectedProduct.getPoids()));
        quantiteField.setText(String.valueOf(selectedProduct.getQuantiteStock()));
    }
    private void filterProducts(String searchText) {
        filteredProducts.setPredicate(product -> {
            if (searchText == null || searchText.isEmpty()) return true;
            String lowerCaseFilter = searchText.toLowerCase();
            if (product.getNom().toLowerCase().contains(lowerCaseFilter)) return true;
            if (product.getDescription().toLowerCase().contains(lowerCaseFilter)) return true;
            return false;
        });
    }
    @FXML

    private void updateImageView(Produit selectedProduct) {
        if (selectedProduct != null) {
            String imagePath = selectedProduct.getImagePath();
            System.out.println("Image path: " + imagePath);
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    productImageView.setImage(image);
                } else {
                    productImageView.setImage(null);
                    System.out.println("Image file not found: " + imagePath);
                }
            } else {
                productImageView.setImage(null);
            }
        } else {
            productImageView.setImage(null);
        }
    }
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    private void configureTableColumns() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        poidsColumn.setCellValueFactory(new PropertyValueFactory<>("poids"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        produitsTable.setItems(produits);
    }
    private void loadProduits() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produit")) {
            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getString("Nom"),
                        rs.getString("Description"),
                        rs.getFloat("PRIX"),
                        rs.getFloat("poids"),
                        rs.getInt("quantite_stock")
                );
                produit.setImagePath(rs.getString("image_path"));
                produits.add(produit);
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Problem loading products: " + ex.getMessage());
        }
    }
    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            imagePreview.setImage(new Image(selectedImageFile.toURI().toString()));
        }
    }
    @FXML
    private void ajouterProduit() {
        if (selectedImageFile != null) {
            Produit produit = getFieldsValues();
            if (produit != null) {
                // Set the imagePath field with the path of the selected image file
                produit.setImagePath(selectedImageFile.getPath());
                insertProduct(produit, selectedImageFile);
                produits.add(produit);
                produitsTable.refresh();
                clearFields();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Image Required", "Please select an image for the product.");
        }
    }

    @FXML
    public void modifierProduit() {
        Produit selectedProduit = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduit != null) {
            // Update product details from fields
            updateProduitDetails(selectedProduit);

            // Update the product in the database
            updateProduitInDB(selectedProduit);

            // Refresh the table to show updated details
            produitsTable.refresh();
        } else {
            System.out.println("No product selected for modification.");
        }
    }
    private void updateProduitDetails(Produit produit) {
        produit.setNom(nomField.getText());
        produit.setDescription(descField.getText());
        produit.setPrix(Float.parseFloat(prixField.getText()));
        produit.setPoids(Float.parseFloat(poidsField.getText()));
        produit.setQuantiteStock(Integer.parseInt(quantiteField.getText()));
    }
    private void updateProduitInDB(Produit produit) {
        String sql = "UPDATE produit SET Nom = ?, Description = ?, PRIX = ?, poids = ?, quantite_stock = ?, image_path = ? WHERE ID_produit = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, produit.getNom());
            statement.setString(2, produit.getDescription());
            statement.setFloat(3, produit.getPrix());
            statement.setFloat(4, produit.getPoids());
            statement.setInt(5, produit.getQuantiteStock());
            statement.setString(6, produit.getImagePath());  // Assuming image path is not changed here
            statement.setInt(7, produit.getIdProduit());  // Assuming you have a getProduitID() method

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void supprimerProduit() {
        Produit selectedProduit = produitsTable.getSelectionModel().getSelectedItem();
        if (selectedProduit != null) {
            deleteProduct(selectedProduit);
            produits.remove(selectedProduit);
        }
    }
    private void updateProduit(Produit produit) {
        if (selectedImageFile == null) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "No image selected for update.");
            return;
        }
        String sql = "UPDATE produit SET Nom = ?, Description = ?, PRIX = ?, poids = ?, quantite_stock = ?, image_produit = ? WHERE ID_produit = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             FileInputStream inputStream = new FileInputStream(selectedImageFile)) {

            stmt.setString(1, produit.getNom());
            stmt.setString(2, produit.getDescription());
            stmt.setDouble(3, produit.getPrix());
            stmt.setDouble(4, produit.getPoids());
            stmt.setInt(5, produit.getQuantiteStock());
            stmt.setBinaryStream(6, inputStream, (int) selectedImageFile.length());
            stmt.setInt(7, produit.getIdProduit());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                loadProduits();
                clearFields();
            }
        } catch (SQLException | IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update product: " + ex.getMessage());
        }
    }
    private boolean deleteProduct(Produit produit) {
        String sql = "DELETE FROM produit WHERE Nom = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produit.getNom());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                produits.remove(produit);
                produitsTable.refresh();
                return true;
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete product: " + ex.getMessage());
        }
        return false;
    }
    private void insertProduct(Produit produit, File imageFile) {
        String sql = "INSERT INTO produit (Nom, Description, PRIX, image_produit, poids, quantite_stock, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             FileInputStream inputStream = new FileInputStream(imageFile)) {

            stmt.setString(1, produit.getNom());
            stmt.setString(2, produit.getDescription());
            stmt.setFloat(3, produit.getPrix());
            stmt.setFloat(5, produit.getPoids());
            stmt.setInt(6, produit.getQuantiteStock());
            stmt.setBinaryStream(4, inputStream, (int) imageFile.length());
            stmt.setString(7, produit.getImagePath());  // Store the imagePath in the database

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produit.setIdProduit(generatedKeys.getInt(1));
                    }
                }
                produits.add(produit);
                clearFields();
                produitsTable.refresh();
            }
        } catch (SQLException | IOException ex) {
            System.out.println(ex.getMessage());;
        }
    }
    private Produit getFieldsValues() {
        try {
            String nom = nomField.getText();
            String desc = descField.getText();
            float prix = Float.parseFloat(prixField.getText());
            float poids = Float.parseFloat(poidsField.getText());
            int quantite = Integer.parseInt(quantiteField.getText());
            // Check if image is loaded and has a URL
            String imageUrl = (imagePreview.getImage() != null) ? imagePreview.getImage().getUrl() : null;
            if (imageUrl == null) {
                showAlert(Alert.AlertType.ERROR, "Image Error", "No image selected.");
                return null;
            }
            return new Produit(nom, desc, prix, poids, quantite);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numerical values for price, weight, and quantity.");
            return null;
        }
    }
    @FXML
    private void clearFields() {
        nomField.clear();
        descField.clear();
        prixField.clear();
        poidsField.clear();
        quantiteField.clear();
        imagePreview.setImage(null);
    }

    private void setupSearchFilter() {
        filteredProducts = new FilteredList<>(produits, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });
        produitsTable.setItems(filteredProducts);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
