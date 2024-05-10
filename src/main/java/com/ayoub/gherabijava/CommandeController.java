package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CommandeController implements Initializable {
    private Parent root;

    @FXML
    private Label pageTitle;

    @FXML
    private VBox commandesList;

    @FXML
    private String handleDialogBox(String headerText){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Nom du Client");
        textInputDialog.setHeaderText(headerText);
        textInputDialog.getDialogPane().setContentText("Entrez Le Nom Du Client:");
        Optional<String> response = textInputDialog.showAndWait();
        String input;
        if(response.isPresent()) {
            input = response.get();
            if (input.trim().isEmpty()) {
                input = handleDialogBox("invalid input value");
            }
        }else{
            input = handleDialogBox("Nom Du Client");
        }

        return input.trim();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String clientName = handleDialogBox("Nom Du Client");
        String[] commandes = {"c1","c2","c3"};
        Label commandeLabel;
        HBox labelContainer;

        showCommandes();
    }

    public void showCommandes(){

    }
}
