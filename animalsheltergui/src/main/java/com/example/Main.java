package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.controllers.MainController;
import com.example.controllers.LoginController;
import com.example.dao.AnimalDao;
import com.example.dao.AnimalDaoImpl;
import com.example.dao.AnimalShelterDao;
import com.example.dao.AnimalShelterDaoImpl;
import com.example.dao.RatingDao;
import com.example.dao.RatingDaoImpl;
import com.example.model.Animal;
import com.example.model.AnimalShelter;
import com.example.model.Rating;
import com.example.service.SerializationService;
import com.example.service.ShelterService;
import com.example.service.BackupService;

public class Main extends Application {
    private static Scene scene;
    private int width = 1020;
    private int height = 640;
    private static SessionFactory sessionFactory;
    private static ShelterService shelterService;
    private static SerializationService serializationService;
    private static BackupService backupService;
    private static RatingDao ratingDao;

    public static RatingDao getRatingDao() {
        return ratingDao;
    }

    private void initializeDatabase() {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Animal.class)
                    .addAnnotatedClass(AnimalShelter.class)
                    .addAnnotatedClass(Rating.class)
                    .buildSessionFactory();

            AnimalDao animalDao = new AnimalDaoImpl(sessionFactory);
            AnimalShelterDao animalShelterDao = new AnimalShelterDaoImpl(sessionFactory);
            ratingDao = new RatingDaoImpl(sessionFactory);

            serializationService = new SerializationService();
            backupService = new BackupService(serializationService);
            shelterService = new ShelterService(animalDao, animalShelterDao, sessionFactory, backupService);

        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        fxmlLoader.setControllerFactory(param -> {
            if (param == MainController.class) {
                return new MainController(shelterService, ratingDao);
            } else if (param == LoginController.class) {
                return new LoginController();
            }
            return null;
        });

        scene = new Scene(fxmlLoader.load(), width, height);
        stage.setScene(scene);
        stage.setTitle("Animal Shelter");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static ShelterService getShelterService() {
        return shelterService;
    }
}