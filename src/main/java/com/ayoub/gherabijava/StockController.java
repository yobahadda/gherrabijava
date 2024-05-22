package com.ayoub.gherabijava;

import com.ayoub.gherabijava.models.Produit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StockController implements Initializable {
    @FXML
    private VBox productContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadStockData();
    }

    private void loadStockData() {
        String url = "jdbc:mysql://localhost:3306/projet_java";
        String user = "root";
        String password = "";

        String sql = "SELECT * FROM produits";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String productName = resultSet.getString("nom");
                int quantity = resultSet.getInt("quantiteStock");
                String imageUrl = resultSet.getString("imageUrl"); // Assuming you have an imageUrl column

                Produit product = new Produit();
                product.setNom(productName);
                product.setQuantiteStock(quantity);

                HBox productCard = createProductCard(productName, quantity, imageUrl);
                productContainer.getChildren().add(productCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HBox createProductCard(String productName, int quantity, String imageUrl) {
        HBox card = new HBox(10);

        ImageView productImage = new ImageView(new Image(imageUrl));
        productImage.setFitHeight(100);
        productImage.setFitWidth(100);

        Label nameLabel = new Label(productName);
        Label quantityLabel = new Label("Quantity: " + quantity);

        card.getChildren().addAll(productImage, nameLabel, quantityLabel);

        return card;
    }
}