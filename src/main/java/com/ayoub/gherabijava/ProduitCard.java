package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.MalformedURLException;

public class ProduitCard {
    @FXML
    private ImageView imageView;
    @FXML
    private Label nomProduit;

    @FXML
    private Label prixProduit;

    @FXML
    private AnchorPane card;

    private CommandeController commandeController;

    public void setCommandeController(CommandeController commandeController) {
        this.commandeController = commandeController;
    }


    public void setCardData(String nom, float prix,String imagePath){
        nomProduit.setText(nom);
        prixProduit.setText(String.valueOf(prix));
        setImage(imagePath);
        card.setOnMouseClicked(this::handleCardClick);
    }

private void setImage(String imagePath) {
    if (imagePath != null && !imagePath.isEmpty()) {
        try {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toURL().toString());
            imageView.setImage(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("error 202");
    }
}

    private void handleCardClick(MouseEvent event) {
        commandeController.workCardData(nomProduit.getText(), Float.parseFloat(prixProduit.getText()));
    }
}
