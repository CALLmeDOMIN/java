package com.example.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import com.example.dao.AnimalDao;
import com.example.dao.AnimalShelterDao;

import com.example.exceptions.AnimalAlreadyExistsException;
import com.example.exceptions.AnimalNotFoundException;
import com.example.exceptions.ShelterFullException;
import com.example.model.Animal;
import com.example.model.AnimalCondition;
import com.example.model.AnimalShelter;
import com.example.model.Rating;
import com.example.utils.AlertUtils;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.Hibernate;

public class ShelterService {
    private final AnimalDao animalDao;
    private final AnimalShelterDao animalShelterDao;
    private final SessionFactory sessionFactory;
    private final BackupService backupService;

    public ShelterService(AnimalDao animalDao, AnimalShelterDao animalShelterDao, SessionFactory sessionFactory,
            BackupService backupService) {
        this.animalDao = animalDao;
        this.animalShelterDao = animalShelterDao;
        this.sessionFactory = sessionFactory;
        this.backupService = backupService;
    }

    public List<AnimalShelter> getAllShelters() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                List<AnimalShelter> shelters = session.createQuery("FROM AnimalShelter", AnimalShelter.class).list();
                List<AnimalShelter> detachedShelters = new ArrayList<>();

                for (AnimalShelter shelter : shelters) {
                    Hibernate.initialize(shelter);
                    Hibernate.initialize(shelter.getAnimals());
                    Hibernate.initialize(shelter.getRatings());

                    AnimalShelter detachedShelter = new AnimalShelter();
                    detachedShelter.setId(shelter.getId());
                    detachedShelter.setName(shelter.getName());
                    detachedShelter.setCapacity(shelter.getCapacity());

                    // Copy animals
                    for (Animal animal : shelter.getAnimals()) {
                        Animal detachedAnimal = new Animal();
                        detachedAnimal.setId(animal.getId());
                        detachedAnimal.setName(animal.getName());
                        detachedAnimal.setSpecies(animal.getSpecies());
                        detachedAnimal.setAge(animal.getAge());
                        detachedAnimal.setWeight(animal.getWeight());
                        detachedAnimal.setCondition(animal.getCondition());
                        detachedAnimal.setShelter(detachedShelter);
                        detachedShelter.getAnimals().add(detachedAnimal);
                    }

                    for (Rating rating : shelter.getRatings()) {
                        Rating detachedRating = new Rating();
                        detachedRating.setId(rating.getId());
                        detachedRating.setValue(rating.getValue());
                        detachedRating.setComment(rating.getComment());
                        detachedRating.setShelter(detachedShelter);
                        detachedShelter.getRatings().add(detachedRating);
                    }

                    detachedShelters.add(detachedShelter);
                }

                tx.commit();
                return detachedShelters;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public AnimalShelter getShelterById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                AnimalShelter shelter = session.get(AnimalShelter.class, id);
                if (shelter != null) {
                    Hibernate.initialize(shelter);
                    Hibernate.initialize(shelter.getAnimals());
                    Hibernate.initialize(shelter.getRatings());

                    AnimalShelter detachedShelter = new AnimalShelter();
                    detachedShelter.setId(shelter.getId());
                    detachedShelter.setName(shelter.getName());
                    detachedShelter.setCapacity(shelter.getCapacity());

                    for (Animal animal : shelter.getAnimals()) {
                        Animal detachedAnimal = new Animal();
                        detachedAnimal.setId(animal.getId());
                        detachedAnimal.setName(animal.getName());
                        detachedAnimal.setSpecies(animal.getSpecies());
                        detachedAnimal.setAge(animal.getAge());
                        detachedAnimal.setWeight(animal.getWeight());
                        detachedAnimal.setCondition(animal.getCondition());
                        detachedAnimal.setShelter(detachedShelter);
                        detachedShelter.getAnimals().add(detachedAnimal);
                    }

                    for (Rating rating : shelter.getRatings()) {
                        Rating detachedRating = new Rating();
                        detachedRating.setId(rating.getId());
                        detachedRating.setValue(rating.getValue());
                        detachedRating.setComment(rating.getComment());
                        detachedRating.setShelter(detachedShelter);
                        detachedShelter.getRatings().add(detachedRating);
                    }

                    tx.commit();
                    return detachedShelter;
                }
                tx.commit();
                return null;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public void saveShelter(AnimalShelter shelter) {
        animalShelterDao.save(shelter);
    }

    public void updateShelter(AnimalShelter shelter) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(shelter);
            tx.commit();
        }
    }

    public void deleteShelter(AnimalShelter shelter) {
        animalShelterDao.delete(shelter);
    }

    public List<AnimalShelter> findSheltersByName(String name) {
        return animalShelterDao.findByName(name);
    }

    public Animal getAnimalById(Long id) {
        return animalDao.findById(id);
    }

    public void saveAnimal(Animal animal) {
        animalDao.save(animal);
    }

    public void updateAnimal(Animal animal) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(animal);
            tx.commit();
        }
    }

    public void deleteAnimal(Animal animal) {
        animalDao.delete(animal);
    }

    public List<Animal> findAnimalsByName(String name) {
        return animalDao.findByName(name);
    }

    public List<Animal> findAnimalsBySpecies(String species) {
        return animalDao.findBySpecies(species);
    }

    public List<Animal> findAnimalsByCondition(AnimalCondition condition) {
        return animalDao.findByCondition(condition);
    }

    public void addAnimalToShelter(AnimalShelter shelter, Animal animal)
            throws ShelterFullException, AnimalAlreadyExistsException {
        if (shelter.getAnimalCount() >= shelter.getCapacity()) {
            throw new ShelterFullException("Shelter is full");
        }
        if (shelter.getAnimalList().contains(animal)) {
            throw new AnimalAlreadyExistsException("Animal already exists in the shelter");
        }

        shelter.addAnimal(animal);
        updateShelter(shelter);
    }

    public void removeAnimalFromShelter(AnimalShelter shelter, Animal animal) throws AnimalNotFoundException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            AnimalShelter managedShelter = session.get(AnimalShelter.class, shelter.getId());
            Animal managedAnimal = session.get(Animal.class, animal.getId());

            if (managedShelter == null || managedAnimal == null) {
                throw new AnimalNotFoundException("Animal or shelter not found");
            }

            managedShelter.getAnimals().remove(managedAnimal);
            managedAnimal.setShelter(null);

            session.merge(managedShelter);
            session.merge(managedAnimal);

            tx.commit();

            shelter.getAnimals().remove(animal);
            animal.setShelter(null);
        }
    }

    public void importShelters(List<AnimalShelter> shelters) throws IOException {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory not initialized");
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                if (backupService == null) {
                    throw new IllegalStateException("BackupService not initialized");
                }

                List<AnimalShelter> currentShelters = getAllShelters();
                backupService.createBackup(currentShelters);

                session.createMutationQuery("DELETE FROM Rating").executeUpdate();
                session.createMutationQuery("DELETE FROM Animal").executeUpdate();
                session.createMutationQuery("DELETE FROM AnimalShelter").executeUpdate();

                for (AnimalShelter shelter : shelters) {
                    AnimalShelter newShelter = new AnimalShelter(shelter.getName(), shelter.getCapacity());

                    if (shelter.getAnimals() != null) {
                        Set<Animal> newAnimals = new HashSet<>();
                        for (Animal animal : shelter.getAnimals()) {
                            Animal newAnimal = new Animal();
                            newAnimal.setName(animal.getName());
                            newAnimal.setSpecies(animal.getSpecies());
                            newAnimal.setAge(animal.getAge());
                            newAnimal.setWeight(animal.getWeight());
                            newAnimal.setCondition(animal.getCondition());
                            newAnimal.setShelter(newShelter);
                            newAnimals.add(newAnimal);
                        }
                        newShelter.setAnimals(newAnimals);
                    }

                    if (shelter.getRatings() != null) {
                        Set<Rating> newRatings = new HashSet<>();
                        for (Rating rating : shelter.getRatings()) {
                            Rating newRating = new Rating();
                            newRating.setValue(rating.getValue());
                            newRating.setComment(rating.getComment());
                            newRating.setShelter(newShelter);
                            newRatings.add(newRating);
                        }
                        newShelter.setRatings(newRatings);
                    }

                    session.persist(newShelter);
                }

                tx.commit();
                AlertUtils.showInfo("Success", "Shelters imported successfully");
            } catch (Exception e) {
                tx.rollback();
                AlertUtils.showError("Error", "Import failed: " + e.getMessage() +
                        "\nPrevious data backed up in 'backups' directory");
                throw e;
            }
        }
    }

    public List<Object[]> getShelterDataForExport() {
        try (Session session = sessionFactory.openSession()) {
            String hql = """
                    SELECT s.name, s.capacity,
                    COUNT(a),
                    (SELECT AVG(r.value) FROM Rating r WHERE r.shelter = s)
                    FROM AnimalShelter s
                    LEFT JOIN s.animals a
                    GROUP BY s.id, s.name, s.capacity
                    """;
            return session.createQuery(hql, Object[].class).list();
        }
    }

    public void exportToCSV(String filePath) throws IOException {
        List<Object[]> data = getShelterDataForExport();
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Name,Capacity,Animals Count,Average Rating\n");
            for (Object[] row : data) {
                String line = String.format("%s,%d,%d,%.2f\n",
                        escapeCSV(row[0].toString()),
                        row[1],
                        row[2],
                        row[3] != null ? row[3] : 0.0);
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
}
