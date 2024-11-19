package com.example.controllers;

import com.example.animalshelter.Animal;
import com.example.animalshelter.AnimalCondition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddAnimalDialogController {
    private Animal animal;
    private Stage dialogStage;

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

    @FXML
    private Text errorText;

    @FXML
    public void initialize() {
        conditionField.setItems(FXCollections.observableArrayList(
                AnimalCondition.HEALTHY.toString(),
                AnimalCondition.SICK.toString(),
                AnimalCondition.QUARANTINED.toString(),
                AnimalCondition.ADOPTED.toString()));

        priceField.setOnAction(event -> handleSave());
    }

    @FXML
    private void handleSave() {
        if (!isValidInput()) {
            return;
        }

        animal = new Animal(nameField.getText(), speciesField.getText(),
                AnimalCondition.valueOf(conditionField.getValue()),
                Integer.parseInt(ageField.getText()), Double.parseDouble(priceField.getText()));
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Animal getAnimal() {
        return animal;
    }

    public boolean isValidInput() {
        String name = nameField.getText();
        String species = speciesField.getText();
        String condition = conditionField.getValue();
        String age = ageField.getText();
        String price = priceField.getText();

        if (name == null || name.isEmpty() || species == null || species.isEmpty() ||
                condition == null || condition.isEmpty() || age == null || age.isEmpty() ||
                price == null || price.isEmpty()) {
            errorText.setText("Please fill in all fields");
            return false;
        }

        try {
            Integer.parseInt(age);
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            errorText.setText("Wrong input format");
            return false;
        }

        return true;
    }
}
