package com.example.animalshelter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShelterManager {
    public Map<String, AnimalShelter> shelters;
    
    public ShelterManager() {
        shelters = new HashMap<>();
    }

    public void addShelter(AnimalShelter shelter) {
        shelters.put(shelter.getShelterName(), shelter);
    }

    public void removeShelter(String name) {
        shelters.remove(name);
    }

    public List<AnimalShelter> findEmpty() {
        List<AnimalShelter> emptyShelters = new ArrayList<>();

        for (Map.Entry<String, AnimalShelter> entry : shelters.entrySet()) {
            if (entry.getValue().getAnimalCount() == 0) {
                emptyShelters.add(entry.getValue());
            }
        }

        return emptyShelters;
    }
}
