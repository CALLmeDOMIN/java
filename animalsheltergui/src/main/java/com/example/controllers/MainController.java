package com.example.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private TableColumn<Animal, Double> priceColumn;

    @FXML
    private TableColumn<AnimalShelter, String> actionsColumn;

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

    @FXML
    private Button addShelterButton;

    @FXML
    private Button addAnimalButton;

    @FXML
    private ComboBox<String> conditionComboBox;

    private ShelterManager shelterManager;
    private ObservableList<Animal> animalList;
    private ObservableList<AnimalShelter> shelterList;

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

        initializeTableColumns(role);
        initializeShelterManager();

        shelterList = FXCollections.observableArrayList(shelterManager.shelters.values());
        animalList = FXCollections.observableArrayList();

        shelterTableView.setItems(shelterList);
        tableView.setItems(animalList);

        shelterTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                animalList.setAll(newValue.getAnimalList());
            } else {
                animalList.clear();
            }
        });

        addAnimalButton.setOnAction(event -> addAnimal());
        addShelterButton.setOnAction(event -> addShelter());

        conditionComboBox.setItems(FXCollections.observableArrayList(
                "All",
                AnimalCondition.HEALTHY.toString(),
                AnimalCondition.SICK.toString(),
                AnimalCondition.QUARANTINED.toString(),
                AnimalCondition.ADOPTED.toString()));
        conditionComboBox.setValue("All");
        conditionComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> filterAnimalsByCondition(newValue));
    }

    private void initializeTableColumns(Role role) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("shelterName"));

        if (role == Role.ADMIN) {
            actionsColumn.setVisible(true);
            actionsColumn.setCellFactory(getActionsCellFactory("animal"));
            shelterActionsColumn.setVisible(true);
            shelterActionsColumn.setCellFactory(getActionsCellFactory("shelter"));
            shelterCapacityColumn.setVisible(true);
            shelterCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        } else {
            actionsColumn.setVisible(false);
            shelterActionsColumn.setVisible(false);
            shelterCapacityColumn.setVisible(false);
        }
    }

    public void initializeShelterManager() {
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
    }

    private void refreshTableView(String type) {
        if (type.equals("animal")) {
            AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
            if (selectedShelter != null) {
                animalList.setAll(selectedShelter.getAnimalList());
            }
        } else if (type.equals("shelter")) {
            shelterList.setAll(shelterManager.shelters.values());
        }
    }

    private <T> Callback<TableColumn<T, String>, TableCell<T, String>> getActionsCellFactory(String type) {
        return param -> new TableCell<T, String>() {
            private final Button deleteButton = new Button("\uD83D\uDDD1");
            private final Button editButton = new Button("\u270F");

            {
                editButton.setOnAction(event -> {
                    T item = getTableView().getItems().get(getIndex());
                    if (type.equals("animal")) {
                        Animal animal = (Animal) item;
                        editAnimal(animal);
                    } else if (type.equals("shelter")) {
                        AnimalShelter shelter = (AnimalShelter) item;
                        editShelter(shelter);
                    }
                });
            }

            {
                deleteButton.setOnAction(event -> {
                    T item = getTableView().getItems().get(getIndex());
                    if (type.equals("animal")) {
                        Animal animal = (Animal) item;
                        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
                        if (selectedShelter != null) {
                            selectedShelter.removeAnimal(animal);
                            refreshTableView("animal");
                        }
                    } else if (type.equals("shelter")) {
                        AnimalShelter shelter = (AnimalShelter) item;
                        shelterManager.removeShelter(shelter.getShelterName());
                        refreshTableView("shelter");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(editButton);
                    hbox.getChildren().add(deleteButton);
                    setGraphic(hbox);
                }
            }
        };
    }

    private void editAnimal(Animal animal) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("editAnimalDialog.fxml"));
            Parent root = loader.load();

            EditAnimalDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            controller.setDialogStage(dialogStage);
            controller.setAnimal(animal);

            dialogStage.setTitle("Edit Animal");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            refreshTableView("animal");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editShelter(AnimalShelter shelter) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("editShelterDialog.fxml"));
            Parent root = loader.load();

            EditShelterDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            controller.setDialogStage(dialogStage);
            controller.setShelter(shelter);

            dialogStage.setTitle("Edit Shelter");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(shelterTableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            refreshTableView("shelter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAnimal() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("addAnimalDialog.fxml"));
            Parent root = loader.load();

            AddAnimalDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            controller.setDialogStage(dialogStage);

            dialogStage.setTitle("Add Animal");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
            if (selectedShelter != null) {
                selectedShelter.addAnimal(controller.getAnimal());
                refreshTableView("animal");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addShelter() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("addShelterDialog.fxml"));
            Parent root = loader.load();

            AddShelterDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            controller.setDialogStage(dialogStage);

            dialogStage.setTitle("Add Shelter");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(shelterTableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            shelterManager.addShelter(controller.getShelter());
            refreshTableView("shelter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterAnimalsByCondition(String condition) {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter != null) {
            if (condition.equals("All")) {
                animalList.setAll(selectedShelter.getAnimalList());
            } else {
                AnimalCondition selectedCondition = AnimalCondition.valueOf(condition);
                List<Animal> filteredAnimals = selectedShelter.getAnimalList().stream()
                        .filter(animal -> animal.getCondition() == selectedCondition).collect(Collectors.toList());
                animalList.setAll(filteredAnimals);
            }
        }
    }
}