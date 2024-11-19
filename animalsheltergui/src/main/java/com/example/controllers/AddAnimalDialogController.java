package com.example.controllers;

import com.example.animalshelter.Animal;
import com.example.animalshelter.AnimalCondition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddAnimalDialogController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField speciesField;

    @FXML
    private ComboBox<String> conditionField;

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
    public void initialize() {
        conditionField.setItems(FXCollections.observableArrayList(
                AnimalCondition.HEALTHY.toString(),
                AnimalCondition.SICK.toString(),
                AnimalCondition.QUARANTINED.toString(),
                AnimalCondition.ADOPTED.toString()));
    }

    @FXML
    private void handleSave() {
        try {
            animal = new Animal(nameField.getText(), speciesField.getText(),
                    AnimalCondition.valueOf(conditionField.getValue()),
                    Integer.parseInt(ageField.getText()), Double.parseDouble(priceField.getText()));
            dialogStage.close();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}
