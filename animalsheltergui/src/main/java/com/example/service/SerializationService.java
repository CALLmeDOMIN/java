package com.example.service;

import java.io.*;
import java.util.List;

import com.example.model.Animal;
import com.example.model.AnimalCondition;
import com.example.model.AnimalShelter;
import com.example.model.Rating;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerializationService {
    private static final String SHELTERS_FILE = "shelters.bin";
    private static final String ANIMALS_FILE = "animals.bin";
    private static final String BACKUP_DIR = "backups";

    public <T> void saveToFile(List<T> objects, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(objects);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        }
    }

    public void exportToCsv(List<AnimalShelter> shelters, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Name,Capacity,Animals Count,Average Rating\n");

            for (AnimalShelter shelter : shelters) {
                String line = String.format("%s,%d,%d,%.2f\n",
                        escapeCSV(shelter.getName()),
                        shelter.getCapacity(),
                        shelter.getAnimals().size(),
                        calculateAverageRating(shelter));
                writer.write(line);
            }
        }
    }

    public void exportAnimalsToCsv(List<Animal> animals, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("ID,Name,Species,Age,Weight,Condition\n");

            for (Animal animal : animals) {
                String line = String.format("%s,%s,%s,%d,%.2f,%s\n",
                        animal.getId(),
                        escapeCSV(animal.getName()),
                        escapeCSV(animal.getSpecies()),
                        animal.getAge(),
                        animal.getWeight(),
                        animal.getCondition());
                writer.write(line);
            }
        }
    }

    private String escapeCSV(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private double calculateAverageRating(AnimalShelter shelter) {
        if (shelter.getRatings() == null || shelter.getRatings().isEmpty()) {
            return 0.0;
        }
        return shelter.getRatings().stream()
                .mapToInt(Rating::getValue)
                .average()
                .orElse(0.0);
    }

    public void saveShelters(List<AnimalShelter> shelters) throws IOException {
        saveToFile(shelters, SHELTERS_FILE);
    }

    public void saveAnimals(List<Animal> animals) throws IOException {
        saveToFile(animals, ANIMALS_FILE);
    }

    public List<AnimalShelter> loadShelters() throws IOException, ClassNotFoundException {
        return loadFromFile(SHELTERS_FILE);
    }

    public List<Animal> loadAnimals() throws IOException, ClassNotFoundException {
        return loadFromFile(ANIMALS_FILE);
    }

    public List<AnimalShelter> importSheltersFromCsv(String filename) throws IOException {
        List<AnimalShelter> shelters = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String header = reader.readLine();
            if (header == null) {
                return shelters;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                AnimalShelter shelter = new AnimalShelter();

                shelter.setName(unescapeCSV(values[0]));
                shelter.setCapacity(Integer.parseInt(values[1].trim()));

                shelters.add(shelter);
            }
        }
        return shelters;
    }

    private String unescapeCSV(String value) {
        if (value == null)
            return "";
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replace("\"\"", "\"");
    }

    public List<Animal> importAnimalsFromCsv(String filename) throws IOException {
        List<Animal> animals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String header = reader.readLine();
            if (header == null)
                return animals;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Animal animal = new Animal();

                animal.setName(values[1]);
                animal.setSpecies(values[2]);
                animal.setAge(Integer.parseInt(values[3]));
                animal.setWeight(Double.parseDouble(values[4]));
                animal.setCondition(AnimalCondition.valueOf(values[5]));

                animals.add(animal);
            }
        }
        return animals;
    }

    public void createBackup(List<AnimalShelter> shelters) throws IOException {
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdir();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String shelterBackupPath = BACKUP_DIR + "/shelters_" + timestamp + ".bin";
        saveToFile(shelters, shelterBackupPath);

        String shelterCsvBackupPath = BACKUP_DIR + "/shelters_" + timestamp + ".csv";
        exportToCsv(shelters, shelterCsvBackupPath);

        for (AnimalShelter shelter : shelters) {
            if (!shelter.getAnimals().isEmpty()) {
                String animalBackupPath = BACKUP_DIR + "/animals_" + shelter.getId() + "_" + timestamp + ".bin";
                saveToFile(new ArrayList<>(shelter.getAnimals()), animalBackupPath);
                String animalCsvBackupPath = BACKUP_DIR + "/animals_" + shelter.getId() + "_" + timestamp + ".csv";
                exportAnimalsToCsv(new ArrayList<>(shelter.getAnimals()), animalCsvBackupPath);
            }
        }
    }
}