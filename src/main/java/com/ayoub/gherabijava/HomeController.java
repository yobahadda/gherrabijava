package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomeController {
    private Parent root;

    @FXML
    private Label clockLabel;

    public void initialize() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            clockLabel.setText(currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

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
    private Button signOutBtn; // Make sure this matches the fx:id of your SignOut button

    public void signOut() {
        try {
            Parent adminScene = FXMLLoader.load(getClass().getResource("admin.fxml"));
            // Assuming you have a Stage instance available to switch scenes
            Stage stage = (Stage) signOutBtn.getScene().getWindow();
            stage.setScene(new Scene(adminScene));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
