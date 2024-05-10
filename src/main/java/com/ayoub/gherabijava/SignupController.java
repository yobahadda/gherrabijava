package com.ayoub.gherabijava;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
public class SignupController {
    @FXML
    private TextField id;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField adresseField; // Address
    @FXML
    private TextField emailField; // Email
    @FXML
    private TextField telephoneField; // Phone Number
    @FXML
    private void handleSubmitButtonAction() {
        // This method gets called when the Submit button is clicked

        // You can access the text of each TextField like this:
        String idn= id.getText();
        String firstName = prenomField.getId();
        String lastName = nomField.getText();
        String address = adresseField.getText();
        String email = emailField.getText();
        String phoneNumber = telephoneField.getText();

        // For demonstration purposes, we just print the data to the console
        System.out.println("id: " + idn);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Address: " + address);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNumber);
//        connect("INSERT INTO clients (ClientID,Prenom,Nom,Addresse,Email,telephone) VALUES (5,firstName,lastName,address,email,phoneNumber)");
    }
}
