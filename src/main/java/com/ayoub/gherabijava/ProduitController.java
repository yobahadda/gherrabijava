package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProduitController implements Initializable {
    private Parent root;

    @FXML
    private VBox produitsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] names = {"samsung","badr","iphone"};
        Label produitLabel;
        HBox produitContainer;

    }

    public void showProduits(){

    }
}
