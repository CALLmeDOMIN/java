package com.example.controllers;

import com.example.animalshelter.Animal;
import com.example.animalshelter.AnimalCondition;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditAnimalDialogController {
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

        animal.setName(nameField.getText());
        animal.setSpecies(speciesField.getText());
        animal.setCondition(AnimalCondition.valueOf(conditionField.getValue()));
        animal.setAge(Integer.parseInt(ageField.getText()));
        animal.setPrice(Double.parseDouble(priceField.getText()));
        dialogStage.close();
    }

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

    public boolean isValidInput() {
        String name = nameField.getText();
        String species = speciesField.getText();
        String condition = conditionField.getValue();
        String age = ageField.getText();
        String price = priceField.getText();

        if (name == null || name.isEmpty() || species == null || species.isEmpty() || condition == null
                || age == null || age.isEmpty() || price == null || price.isEmpty()) {
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