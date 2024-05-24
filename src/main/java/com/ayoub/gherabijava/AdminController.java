package com.ayoub.gherabijava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private Stage Dashboard;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
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
    private boolean verifyLogin(String username, String password) {
        boolean isValid = false;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projet_java", "root", "S8!hos@samQl")) {
            String query = "SELECT * FROM admin WHERE username =? AND password =?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    isValid = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }


@FXML
    private void openDashboard(ActionEvent event) {
        String enteredUsername = usernameField.getText(); // Retrieve the entered username from your form
        String enteredPassword = passwordField.getText(); // Retrieve the entered password from your form

        if (verifyLogin(enteredUsername, enteredPassword)) {
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
        } else {
            // Display an error message
            System.out.println("Invalid username or password.");
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
