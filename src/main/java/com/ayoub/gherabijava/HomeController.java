package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class HomeController {
    private Parent root;

    @FXML
    private BorderPane appUi;

    @FXML
    private void openClientPage(){
        changeScene("clientPage.fxml");
    }

    @FXML
    private void openProduitPage(){
        changeScene("produitPage.fxml");
    }

    @FXML
    private void openCommandePage(){
        changeScene("commandePage.fxml");
    }

    @FXML
    private void openFacturePage(){
        changeScene("facturePage.fxml");
    }

    @FXML
    private void openLivraisonPage(){
        changeScene("livraisonPage.fxml");
    }

    @FXML
    private void changeScene(String fxml){
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        appUi.setCenter(root);
    }
}
