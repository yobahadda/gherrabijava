package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class FactureController implements Initializable {
    private Parent root;

    @FXML
    private Label pageTitle;

    @FXML
    private VBox facturesList;

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

    @FXML
    private String handleChoiceBox(String clientName, String[] choices){
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices[0], choices);

        dialog.setTitle("Les commandes De"+clientName);
        dialog.setContentText("Choisissez La commande Désirée:");

        Optional<String> response = dialog.showAndWait();

        String input;
        if(response.isPresent()) {
            input = response.get();
        }else{
            input = handleChoiceBox(clientName,choices);
        }
        return input;
    }

    public void showFactures(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String clientName = handleDialogBox("Nom Du Client");
        String[] factures = {"f1","f2","f3"};
        String[] commandes = {"c1","c2","c3"};
        String commandeName = handleChoiceBox(clientName,commandes);
        Label factureLabel;
        HBox labelContainer;
        showFactures();
    }
}
