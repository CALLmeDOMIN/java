package com.example.controllers;

import com.example.animalshelter.Animal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddAnimalDialogController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField speciesField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField priceField;

    private Animal animal;
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isValidInput() {
        return !nameField.getText().isEmpty() && !speciesField.getText().isEmpty() && !ageField.getText().isEmpty()
                && !priceField.getText().isEmpty();
    }

    public Animal getAnimal() {
        return animal;
    }

    @FXML
    private void handleSave() {
        try {
            animal = new Animal(nameField.getText(), speciesField.getText(), Integer.parseInt(ageField.getText()),
                    Double.parseDouble(priceField.getText()));
            dialogStage.close();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}
