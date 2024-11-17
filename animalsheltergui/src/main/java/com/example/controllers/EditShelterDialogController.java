package com.example.controllers;

import com.example.animalshelter.AnimalShelter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditShelterDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField maxCapacityField;

    private AnimalShelter shelter;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
        nameField.setText(shelter.getShelterName());
        maxCapacityField.setText(String.valueOf(shelter.getMaxCapacity()));
    }

    @FXML
    private void handleSave() {
        try {
            shelter.setShelterName(nameField.getText());
            shelter.setMaxCapacity(Integer.parseInt(maxCapacityField.getText()));
            dialogStage.close();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}