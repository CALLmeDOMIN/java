package com.example.controllers;

import java.io.IOException;

import com.example.Main;
import com.example.Role;
import com.example.UserSession;
import com.example.animalshelter.Animal;
import com.example.animalshelter.AnimalCondition;
import com.example.animalshelter.AnimalShelter;
import com.example.animalshelter.ShelterManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class MainController {
    @FXML
    private TableView<Animal> tableView;

    @FXML
    private TableColumn<Animal, String> nameColumn;

    @FXML
    private TableColumn<Animal, String> speciesColumn;

    @FXML
    private TableColumn<Animal, String> conditionColumn;

    @FXML
    private TableColumn<Animal, Integer> ageColumn;

    @FXML
    private TableColumn<AnimalShelter, String> actionsColumn;

    @FXML
    private TableColumn<Animal, Double> priceColumn;

    @FXML
    private TableView<AnimalShelter> shelterTableView;

    @FXML
    private TableColumn<AnimalShelter, String> shelterNameColumn;

    @FXML
    private TableColumn<AnimalShelter, Integer> shelterCapacityColumn;

    @FXML
    private TableColumn<AnimalShelter, String> shelterActionsColumn;

    @FXML
    private Text usernameText;

    @FXML
    private Button signoutButton;

    private ShelterManager shelterManager;

    @FXML
    private void logOut() throws IOException {
        Main.setRoot("login");
    }

    @FXML
    void initialize() {
        UserSession userSession = UserSession.getInstance();
        String username = userSession.getUsername();
        Role role = userSession.getRole();

        usernameText.setText("Welcome, " + username);

        signoutButton.setOnAction(event -> {
            try {
                logOut();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("shelterName"));

        if (role == Role.ADMIN) {
            actionsColumn.setVisible(true);
            actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
            shelterActionsColumn.setVisible(true);
            shelterActionsColumn.setCellFactory(getActionsCellFactory());
            shelterCapacityColumn.setVisible(true);
            shelterCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        } else {
            actionsColumn.setVisible(false);
            shelterActionsColumn.setVisible(false);
            shelterCapacityColumn.setVisible(false);
        }

        shelterManager = new ShelterManager();
        AnimalShelter shelter = new AnimalShelter("My Shelter", 10);
        shelter.addAnimal(new Animal("Buddy", "Dog", AnimalCondition.HEALTHY, 3, 150.0));
        shelter.addAnimal(new Animal("Mittens", "Cat", AnimalCondition.SICK, 2, 100.0));
        shelter.addAnimal(new Animal("Tweety", "Bird", AnimalCondition.HEALTHY, 1, 50.0));

        AnimalShelter shelter2 = new AnimalShelter("Second Shelter", 5);
        shelter2.addAnimal(new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 5, 200.0));
        shelter2.addAnimal(new Animal("Whiskers", "Cat", AnimalCondition.SICK, 4, 150.0));
        shelter2.addAnimal(new Animal("Polly", "Bird", AnimalCondition.HEALTHY, 2, 75.0));

        shelterManager.addShelter(shelter);
        shelterManager.addShelter(shelter2);

        ObservableList<AnimalShelter> shelterItems = FXCollections
                .observableArrayList(shelterManager.shelters.values());
        shelterTableView.setItems(shelterItems);

        shelterTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<Animal> animalItems = FXCollections.observableArrayList(newValue.getAnimalList());
                tableView.setItems(animalItems);
            }
        });
    }

    private Callback<TableColumn<AnimalShelter, String>, TableCell<AnimalShelter, String>> getActionsCellFactory() {
        return param -> new TableCell<AnimalShelter, String>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    AnimalShelter shelter = getTableView().getItems().get(getIndex());
                    shelterManager.removeShelter(shelter.getShelterName());
                    getTableView().setItems(FXCollections.observableArrayList(shelterManager.shelters.values()));
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(deleteButton);
                    setGraphic(hbox);
                }
            }
        };
    }
}