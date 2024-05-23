package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class CamionCard {
    @FXML
    private ImageView camionImg;

    @FXML
    private Label adresseCamion;

    @FXML
    private Label poidsCamion;

    @FXML
    private AnchorPane card;

    private int camionId;

    private LivraisonController livraisonController;

    public void setCamionController(LivraisonController livraisonController) {
        this.livraisonController = livraisonController;
    }

    public void setCardData(String adresse, float poidsTotal, float poidsCourant, int camionId) {
        this.camionId = camionId;
        adresseCamion.setText(adresse);
        poidsCamion.setText(String.valueOf(poidsTotal - poidsCourant));
        String imagePath = getImagePath(poidsTotal, poidsCourant);
        try {
            camionImg.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (NullPointerException e) {
            System.err.println("Image not found: " + imagePath);
            // Optionally, you can set a default image here
            camionImg.setImage(new Image(getClass().getResource("/com/ayoub/gherabijava/images/red.jpg").toExternalForm()));
        }
        card.setOnMouseClicked(this::handleCardClick);
    }

    private String getImagePath(float poidsTotal, float poidsCourant) {
        float poids = poidsTotal - poidsCourant;
        if (poids <= 10) {
            return "/com/ayoub/gherabijava/images/red.jpg";
        } else if (poids > 10 && poids < poidsTotal - 10) {
            return "/com/ayoub/gherabijava/images/green.jpg";
        } else if (poids >= poidsTotal - 10 && poids <= poidsTotal) {
            return "/com/ayoub/gherabijava/images/gray.jpg";
        }
        return ""; // Ensure this file exists
    }

    private void handleCardClick(MouseEvent event) {
        livraisonController.workCardData(adresseCamion.getText(), camionId);
    }
}
