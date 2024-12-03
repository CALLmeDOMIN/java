package com.example.controllers;

import com.example.Main;
import com.example.Role;
import com.example.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signinButton;
    @FXML
    private Text errorText;

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidCredentials(username, password)) {
            try {
                UserSession.getInstance().setUsername(username);

                if (username.equals("admin")) {
                    UserSession.getInstance().setRole(Role.ADMIN);
                } else {
                    UserSession.getInstance().setRole(Role.USER);
                }

                FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));

                loader.setControllerFactory(param -> {
                    if (param == MainController.class) {
                        return new MainController(Main.getShelterService(), Main.getRatingDao());
                    }
                    return null;
                });

                Parent root = loader.load();
                Stage stage = (Stage) signinButton.getScene().getWindow();
                Scene scene = new Scene(root, 1020, 640);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                errorText.setText("Error loading main view: " + e.getMessage());
            }
        } else {
            errorText.setText("Invalid username or password");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        return username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty();
    }
}
