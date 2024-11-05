package com.example.controllers;

import java.io.IOException;

import com.example.Main;
import com.example.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class MainController {
    @FXML
    private Text usernameText;

    @FXML
    private Button signoutButton;

    @FXML
    private void logOut() throws IOException {
        Main.setRoot("login");
    }

    @FXML
    void initialize() {
        UserSession userSession = UserSession.getInstance();
        String username = userSession.getUsername();
        // Role role = userSession.getRole();

        usernameText.setText("Welcome, " + username);

        signoutButton.setOnAction(event -> {
            try {
                logOut();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}