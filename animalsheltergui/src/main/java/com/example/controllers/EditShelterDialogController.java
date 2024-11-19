package com.example.controllers;

import com.example.animalshelter.AnimalShelter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditShelterDialogController {
    private AnimalShelter shelter;
    private Stage dialogStage;

    @FXML
    private TextField nameField;

    @FXML
    private TextField maxCapacityField;

    @FXML
    private Text errorText;

    @FXML
    public void initialize() {
        maxCapacityField.setOnAction(event -> handleSave());
    }

    @FXML
    private void handleSave() {
        if (!isValidInput()) {
            return;
        }

        shelter.setShelterName(nameField.getText());
        shelter.setMaxCapacity(Integer.parseInt(maxCapacityField.getText()));
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setShelter(AnimalShelter shelter) {
        this.shelter = shelter;
        nameField.setText(shelter.getShelterName());
        maxCapacityField.setText(String.valueOf(shelter.getMaxCapacity()));
    }

    private boolean isValidInput() {
        String name = nameField.getText();
        String maxCapacity = maxCapacityField.getText();

        if (name == null || name.isEmpty() || maxCapacity == null || maxCapacity.isEmpty()) {
            errorText.setText("Please fill in all fields");
            return false;
        }

        try {
            Integer.parseInt(maxCapacity);
        } catch (NumberFormatException e) {
            errorText.setText("Wrong input format");
            return false;
        }

        return true;
    }
}