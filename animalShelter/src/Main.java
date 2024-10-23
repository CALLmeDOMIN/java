import java.util.List;

import animalshelter.Animal;
import animalshelter.AnimalCondition;
import animalshelter.AnimalShelter;
import animalshelter.ShelterManager;

public class Main 
{
    public static void main( String[] args )
    {
        Animal animal1 = new Animal("Dog", "Labrador", AnimalCondition.HEALTHY, 5, 100);
        Animal animal2 = new Animal("Cat", "Siamese", AnimalCondition.SICK, 3, 50);
        Animal animal3 = new Animal("Dog", "Poodle", AnimalCondition.HEALTHY, 2, 150);
        AnimalShelter shelter1 = new AnimalShelter("Shelter 1", 10);
        AnimalShelter shelter2 = new AnimalShelter("Shelter 2", 5);
        ShelterManager manager = new ShelterManager();

        manager.addShelter(shelter1);
        manager.addShelter(shelter2);

        shelter1.addAnimal(animal1);
        shelter1.addAnimal(animal2);
        shelter1.addAnimal(animal3);

        shelter1.summary();

        shelter1.removeAnimal(animal1);

        shelter1.summary();

        shelter1.getAnimal(animal2);

        shelter1.summary();

        shelter1.changeCondition(animal3, AnimalCondition.SICK);

        shelter1.summary();

        shelter1.changeAge(animal3, 3);

        shelter1.summary();

        shelter1.addAnimal(animal1);
        shelter1.addAnimal(animal2);

        System.out.println("Sick animals: " + shelter1.countByCondition(AnimalCondition.SICK));

        shelter1.summary();

        System.out.println("Sorted by name:");
        List<Animal> animals = shelter1.sortByName();
        for (Animal a : animals) {
            a.print();
        }

        System.out.println("Sorted by price:");
        animals = shelter1.sortByPrice();
        for (Animal a : animals) {
            a.print();
        }

        System.out.println("Searching for an animal:");
        Animal found = shelter1.search("Dog");
        if (found != null) {
            found.print();
        }

        System.out.println("Searching for an animal partially:");
        animals = shelter1.searchPartial("Do");
        for (Animal a : animals) {
            a.print();
        }

        System.out.println("Searching for an animal that doesn't exist:");
        found = shelter1.search("Fish");
        if (found != null) {
            found.print();
        }

        System.out.println("Max animal:");
        Animal maxAnimal = shelter1.max();
        if (maxAnimal != null) {
            maxAnimal.print();
        }

        manager.summary();

        List<AnimalShelter> emptyShelters = manager.findEmpty();
        System.out.println("Empty shelters:");
        for (AnimalShelter s : emptyShelters) {
            System.out.println(s.getShelterName());
        }

        manager.removeShelter("Shelter 1");

        manager.summary();
    }
}
