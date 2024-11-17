package com.example.controllers;

import com.example.animalshelter.AnimalShelter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddShelterDialogController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField maxCapacityField;

    private AnimalShelter shelter;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public int getMaxCapacity() {
        return Integer.parseInt(maxCapacityField.getText());
    }

    public boolean isValidInput() {
        return !nameField.getText().isEmpty() && Integer.parseInt(maxCapacityField.getText()) > 0;
    }

    public AnimalShelter getShelter() {
        return shelter;
    }

    @FXML
    private void handleSave() {
        try {
            shelter = new AnimalShelter(nameField.getText(), Integer.parseInt(maxCapacityField.getText()));
            dialogStage.close();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}
