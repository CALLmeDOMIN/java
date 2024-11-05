package com.example.controllers;

import java.io.IOException;

import com.example.Main;
import com.example.Role;
import com.example.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {

    @FXML
    private Button signinButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorText;

    @FXML
    private void redirect() throws IOException {
        Main.setRoot("main");
    }

    @FXML
    void initialize() {
        signinButton.setOnAction(event -> {
            formValidation();
        });
    }

    void formValidation() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorText.setText("Please fill in all fields");
        } else {
            Role role = authenticate(username, password);
            if (role != null) {
                UserSession.getInstance().setUsername(username);
                UserSession.getInstance().setRole(role);
                try {
                    redirect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                errorText.setText("Invalid credentials");
            }
        }
    }

    Role authenticate(String username, String password) {
        // ! placeholder for actual credentials check
        String adminCreds = "admin:admin";
        String userCreds = "user:user";
        String creds = username + ":" + password;

        if (adminCreds.equals(creds)) {
            return Role.ADMIN;
        } else if (userCreds.equals(creds)) {
            return Role.USER;
        } else {
            return null;
        }
    }
}
