package animalshelter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnimalShelter {
    private String shelterName;
    private List<Animal> animalList;
    private int maxCapacity;

    public AnimalShelter(String shelterName, int maxCapacity) {
        this.shelterName = shelterName;
        this.maxCapacity = maxCapacity;
        this.animalList = new ArrayList<>();
    }

    public String getShelterName() {
        return shelterName;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getAnimalCount() {
        return animalList.size();
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void addAnimal(Animal animal) {
        if (animalList.size() == maxCapacity) {
            System.err.println("Shelter is full");
            return;
        }
    
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                System.out.println("Animal already exists");
                return;
            }
        }

        animalList.add(animal);
    }

    public void removeAnimal(Animal animal) {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                animalList.remove(a);
                return;
            }
        }

        System.out.println("Animal not found");
    }

    public void getAnimal(Animal animal) {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                a.setCondition(AnimalCondition.ADOPTED);
                animalList.remove(a);
                return;
            }
        }
        System.out.println("Animal not found");
    }

    public void changeCondition(Animal animal, AnimalCondition condition) {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                a.setCondition(condition);
                return;
            }
        }
        System.out.println("Animal not found");
    }

    public void changeAge(Animal animal, int age) {
        for (Animal a : animalList) {
            if (a.compareTo(animal) == 0) {
                a.setAge(age);
                return;
            }
        }
        System.out.println("Animal not found");
    }

    public int countByCondition(AnimalCondition condition) {
        int count = 0;
    
        for (Animal a : animalList)
            if (a.getCondition() == condition)
                count++;
            
        return count;
    }

    public List<Animal> sortByName() {
        List<Animal> sortedByName = new ArrayList<>(animalList);
        Collections.sort(sortedByName);
        return sortedByName;
    }

    public List<Animal> sortByPrice() {
        List<Animal> sortedByPrice = new ArrayList<>(animalList);
        sortedByPrice.sort(Comparator.comparingDouble(Animal::getPrice));
        return sortedByPrice;
    }

    public Animal search(String name) {
        Collections.sort(animalList, Comparator.comparing(Animal::getName));
        Animal searchAnimal = new Animal(name, "", null, 0, 0.0);
        int index = Collections.binarySearch(animalList, searchAnimal, Comparator.comparing(Animal::getName));

        if (index >= 0) {
            return animalList.get(index);
        } else {
            System.out.println("Animal not found");
            return null;
        }
    }

    public List<Animal> searchPartial(String phrase) {
        List<Animal> searchResults = new ArrayList<>();
        phrase = phrase.toLowerCase();
        for (Animal a : animalList) {
            String name = a.getName().toLowerCase();
            String species = a.getSpecies().toLowerCase();

            if (name.contains(phrase) || species.contains(phrase)) {
                searchResults.add(a);
            }
        }
        return searchResults;
    }

    public void summary() {
        System.out.println("Summary:");
        for (Animal a : animalList) {
            a.print();
        }
        System.out.println("=====================================");
    }

    public Animal max() {
        return Collections.max(animalList, Comparator.comparingDouble(Animal::getPrice));
    }
}
