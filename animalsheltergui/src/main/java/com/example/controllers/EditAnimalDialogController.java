package com.example.controllers;

import com.example.animalshelter.Animal;
import com.example.animalshelter.AnimalCondition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditAnimalDialogController {
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

    public void setAnimal(Animal animal) {
        this.animal = animal;
        nameField.setText(animal.getName());
        speciesField.setText(animal.getSpecies());
        conditionField.setValue(animal.getCondition().toString());
        ageField.setText(String.valueOf(animal.getAge()));
        priceField.setText(String.valueOf(animal.getPrice()));
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
            animal.setName(nameField.getText());
            animal.setSpecies(speciesField.getText());
            animal.setCondition(AnimalCondition.valueOf(conditionField.getValue()));
            animal.setAge(Integer.parseInt(ageField.getText()));
            animal.setPrice(Double.parseDouble(priceField.getText()));
            dialogStage.close();
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }
    }
}