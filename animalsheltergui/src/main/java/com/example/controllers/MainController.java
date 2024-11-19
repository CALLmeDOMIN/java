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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController {
    private static final String ALL_CONDITIONS = "All";

    private ShelterManager shelterManager;
    private ObservableList<Animal> animalList;
    private ObservableList<AnimalShelter> shelterList;

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
    private Button addAnimalButton;

    @FXML
    private ComboBox<String> conditionComboBox;

    @FXML
    private TextField animalSearchInput;

    @FXML
    private Button animalSearchButton;

    @FXML
    private Button animalSortButton;

    @FXML
    private TableView<AnimalShelter> shelterTableView;

    @FXML
    private TableColumn<AnimalShelter, String> shelterNameColumn;

    @FXML
    private TableColumn<AnimalShelter, Integer> shelterCapacityColumn;

    @FXML
    private TableColumn<AnimalShelter, String> shelterActionsColumn;

    @FXML
    private Button addShelterButton;

    @FXML
    private TextField shelterSearchInput;

    @FXML
    private Button shelterSearchButton;

    @FXML
    private Button shelterSortButton;

    @FXML
    private Text usernameText;

    @FXML
    private Button signoutButton;

    @FXML
    void initialize() {
        UserSession userSession = UserSession.getInstance();
        String username = userSession.getUsername();
        Role role = userSession.getRole();

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

        usernameText.setText("Welcome, " + username);

        signoutButton.setOnAction(event -> handleLogout());
        addAnimalButton.setOnAction(event -> addAnimal());
        addShelterButton.setOnAction(event -> addShelter());
        shelterSearchButton.setOnAction(event -> searchShelters());
        animalSearchButton.setOnAction(event -> searchAnimals());
        shelterSortButton.setOnAction(event -> sortShelters());
        animalSortButton.setOnAction(event -> sortAnimals());

        animalSearchInput.setOnAction(event -> animalSearchButton.fire());
        shelterSearchInput.setOnAction(event -> shelterSearchButton.fire());

        conditionComboBox.setItems(FXCollections.observableArrayList(
                ALL_CONDITIONS,
                AnimalCondition.HEALTHY.toString(),
                AnimalCondition.SICK.toString(),
                AnimalCondition.QUARANTINED.toString(),
                AnimalCondition.ADOPTED.toString()));
        conditionComboBox.setValue(ALL_CONDITIONS);
        conditionComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> filterAnimalsByCondition(newValue));
    }

    private void handleLogout() {
        try {
            Main.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableColumns(Role role) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        shelterNameColumn.setCellValueFactory(new PropertyValueFactory<>("shelterName"));

        if (role == Role.ADMIN) {
            actionsColumn.setCellFactory(getActionsCellFactory("animal", role));
            shelterActionsColumn.setVisible(true);
            shelterActionsColumn.setCellFactory(getActionsCellFactory("shelter", role));
            shelterCapacityColumn.setVisible(true);
            shelterCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

            addAnimalButton.setVisible(true);
            addShelterButton.setVisible(true);
        } else {
            actionsColumn.setCellFactory(getActionsCellFactory("animal", role));
            shelterActionsColumn.setVisible(false);
            shelterCapacityColumn.setVisible(false);

            addAnimalButton.setVisible(false);
            addShelterButton.setVisible(false);
        }
    }

    public void initializeShelterManager() {
        shelterManager = new ShelterManager();
        AnimalShelter shelter = new AnimalShelter("W My Shelter", 10);
        try {
            shelter.addAnimal(new Animal("Fluffy", "Cat", AnimalCondition.HEALTHY, 2, 100.0));
            shelter.addAnimal(new Animal("Spike", "Dog", AnimalCondition.SICK, 3, 150.0));
            shelter.addAnimal(new Animal("Polly", "Bird", AnimalCondition.HEALTHY, 1, 50.0));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AnimalShelter shelter2 = new AnimalShelter("Second Shelter", 5);
        try {
            shelter2.addAnimal(new Animal("Rex", "Dog", AnimalCondition.HEALTHY, 5, 200.0));
            shelter2.addAnimal(new Animal("Whiskers", "Cat", AnimalCondition.SICK, 4, 150.0));
            shelter2.addAnimal(new Animal("Polly", "Bird", AnimalCondition.HEALTHY, 2, 75.0));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private <T> Callback<TableColumn<T, String>, TableCell<T, String>> getActionsCellFactory(String type, Role role) {
        if (role == Role.USER) {
            return param -> new TableCell<T, String>() {
                private final Button adoptButton = new Button("Adopt");

                {
                    adoptButton.setOnAction(event -> {
                        T item = getTableView().getItems().get(getIndex());
                        Animal animal = (Animal) item;
                        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
                        if (selectedShelter != null) {
                            try {
                                selectedShelter.getAnimal(animal);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            refreshTableView("animal");
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : adoptButton);
                }
            };
        } else {
            return param -> new TableCell<T, String>() {

                private final Button deleteButton = new Button("\uD83D\uDDD1");
                private final Button editButton = new Button("\u270F");

                {
                    editButton.setOnAction(event -> handleEditAction(type, this));
                    deleteButton.setOnAction(event -> handleDeleteAction(type, this));
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : new HBox(editButton, deleteButton));
                }

            };
        }
    }

    private <T> void handleEditAction(String type, TableCell<T, String> cell) {
        T item = cell.getTableView().getItems().get(cell.getIndex());
        if (type.equals("animal")) {
            Animal animal = (Animal) item;
            editAnimal(animal);
        } else if (type.equals("shelter")) {
            AnimalShelter shelter = (AnimalShelter) item;
            editShelter(shelter);
        }
    }

    private <T> void handleDeleteAction(String type, TableCell<T, String> cell) {
        T item = cell.getTableView().getItems().get(cell.getIndex());
        if (type.equals("animal")) {
            Animal animal = (Animal) item;
            AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
            if (selectedShelter != null) {
                try {
                    selectedShelter.removeAnimal(animal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshTableView("animal");
            }
        } else if (type.equals("shelter")) {
            AnimalShelter shelter = (AnimalShelter) item;
            shelterManager.removeShelter(shelter.getShelterName());
            refreshTableView("shelter");
        }
    }

    private void showDialog(String fxmlFile, String title, DialogControllerConsumer controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile + ".fxml"));
            Parent root = loader.load();
            Object controller = loader.getController();
            Stage dialogStage = new Stage();
            controllerConsumer.accept(controller, dialogStage);

            switch (controller.getClass().getSimpleName()) {
                case "EditAnimalDialogController":
                    ((EditAnimalDialogController) controller).setDialogStage(dialogStage);
                    break;
                case "EditShelterDialogController":
                    ((EditShelterDialogController) controller).setDialogStage(dialogStage);
                    break;
                case "AddAnimalDialogController":
                    ((AddAnimalDialogController) controller).setDialogStage(dialogStage);
                    break;
                case "AddShelterDialogController":
                    ((AddShelterDialogController) controller).setDialogStage(dialogStage);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unexpected controller type: " + controller.getClass().getSimpleName());
            }

            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editAnimal(Animal animal) {
        showDialog("editAnimalDialog", "Edit Animal", (controller, dialogStage) -> {
            ((EditAnimalDialogController) controller).setAnimal(animal);
        });
        refreshTableView("animal");
    }

    private void editShelter(AnimalShelter shelter) {
        showDialog("editShelterDialog", "Edit Shelter", (controller, dialogStage) -> {
            ((EditShelterDialogController) controller).setShelter(shelter);
        });
        refreshTableView("shelter");
    }

    private void addAnimal() {
        showDialog("addAnimalDialog", "Add Animal", (controller, dialogStage) -> {
            AddAnimalDialogController addAnimalController = (AddAnimalDialogController) controller;
            addAnimalController.setDialogStage(dialogStage);
            dialogStage.setOnHidden(event -> {
                AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
                if (selectedShelter != null) {
                    Animal newAnimal = addAnimalController.getAnimal();
                    if (newAnimal != null && newAnimal.getName() != null) {
                        try {
                            selectedShelter.addAnimal(newAnimal);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        refreshTableView("animal");
                    } else {
                        System.err.println("Invalid animal data");
                    }
                }
            });
        });
    }

    private void addShelter() {
        showDialog("addShelterDialog", "Add Shelter", (controller, dialogStage) -> {
            AddShelterDialogController addShelterController = (AddShelterDialogController) controller;
            addShelterController.setDialogStage(dialogStage);
            dialogStage.setOnHidden(event -> {
                AnimalShelter newShelter = addShelterController.getShelter();
                if (newShelter != null && newShelter.getShelterName() != null) {
                    shelterManager.addShelter(newShelter);
                    refreshTableView("shelter");
                } else {
                    System.err.println("Invalid shelter data");
                }
            });
        });
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

    private void searchShelters() {
        String searchText = shelterSearchInput.getText().toLowerCase();
        if (searchText.isEmpty()) {
            shelterList.setAll(shelterManager.shelters.values());
        } else {
            List<AnimalShelter> filteredShelters = shelterManager.shelters.values().stream()
                    .filter(shelter -> shelter.getShelterName().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            shelterList.setAll(filteredShelters);
        }
    }

    private void searchAnimals() {
        String searchText = animalSearchInput.getText().toLowerCase();
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();

        if (selectedShelter != null) {
            if (searchText.isEmpty()) {
                animalList.setAll(selectedShelter.getAnimalList());
            } else {
                List<Animal> filteredAnimals = selectedShelter.getAnimalList().stream()
                        .filter(animal -> animal.getName().toLowerCase().contains(searchText)
                                || animal.getSpecies().toLowerCase().contains(searchText))
                        .collect(Collectors.toList());
                animalList.setAll(filteredAnimals);
            }
        }
    }

    private void sortShelters() {
        shelterList.sort((shelter1, shelter2) -> shelter1.getShelterName().compareTo(shelter2.getShelterName()));
    }

    private void sortAnimals() {
        AnimalShelter selectedShelter = shelterTableView.getSelectionModel().getSelectedItem();
        if (selectedShelter != null) {
            List<Animal> sortedAnimals = selectedShelter.sortByName();
            animalList.setAll(sortedAnimals);
        }
    }

    @FunctionalInterface
    private interface DialogControllerConsumer {
        void accept(Object controller, Stage dialogStage);
    }
}