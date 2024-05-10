package com.ayoub.gherabijava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Admin implements Initializable {
    private Stage Dashboard;

    @FXML
    private Button login_btn;

    @FXML
    private Button confirm_btn;

    @FXML
    private Button reset_btn;

    @FXML
    private VBox login_form;

    @FXML
    private VBox confirm_form;

    @FXML
    private VBox reset_form;

    @FXML
    private Button forgot_pass;

    @FXML
    private void showConfirmForm(){
        hideAllForms();
        confirm_form.setVisible(true);
    }

    @FXML
    private void showResetForm(){
        hideAllForms();
        reset_form.setVisible(true);
    }

    @FXML
    private void showLoginForm(){
        hideAllForms();
        login_form.setVisible(true);
    }

    @FXML
    private void openDashboard(ActionEvent event){
        Stage current = (Stage) login_btn.getScene().getWindow();
        Stage dashboard = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            dashboard.setScene(new Scene(root));
            dashboard.setTitle("Premium Rush");
            dashboard.show();
            current.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideAllForms(){
        login_form.setVisible(false);
        confirm_form.setVisible(false);
        reset_form.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showLoginForm();
    }
}
