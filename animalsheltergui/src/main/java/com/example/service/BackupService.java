package com.example.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.model.AnimalShelter;

public class BackupService {
    private static final String BACKUP_DIR = "backups";
    private final SerializationService serializationService;

    public BackupService(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    public void createBackup(List<AnimalShelter> shelters) throws IOException {
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdir();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String shelterBackupPath = BACKUP_DIR + "/shelters_" + timestamp + ".bin";
        serializationService.saveToFile(shelters, shelterBackupPath);

        String shelterCsvBackupPath = BACKUP_DIR + "/shelters_" + timestamp + ".csv";
        serializationService.exportToCsv(shelters, shelterCsvBackupPath);

        for (AnimalShelter shelter : shelters) {
            if (!shelter.getAnimals().isEmpty()) {
                String animalBackupPath = BACKUP_DIR + "/animals_" + shelter.getId() + "_" + timestamp + ".bin";
                serializationService.saveToFile(new ArrayList<>(shelter.getAnimals()), animalBackupPath);

                String animalCsvBackupPath = BACKUP_DIR + "/animals_" + shelter.getId() + "_" + timestamp + ".csv";
                serializationService.exportAnimalsToCsv(new ArrayList<>(shelter.getAnimals()), animalCsvBackupPath);
            }
        }
    }
}