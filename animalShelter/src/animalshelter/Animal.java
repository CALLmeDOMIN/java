package animalshelter;

public class Animal implements Comparable<Animal> {
    private String name;
    private String species;
    private AnimalCondition condition;
    private int age;
    private double price;

    public Animal(String name, String species, AnimalCondition condition, int age, double price) {
        this.name = name;
        this.species = species;
        this.condition = condition;
        this.age = age;
        this.price = price;
    }

    @Override
    public int compareTo(Animal other) {
        int nameComparison = name.compareTo(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }

        int speciesComparison = species.compareTo(other.species);
        if (speciesComparison != 0) {
            return speciesComparison;
        }

        return Integer.compare(this.age, other.age);
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public AnimalCondition getCondition() {
        return condition;
    }

    public void setCondition(AnimalCondition condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return price;
    }
    
    public void setAge(int age) {
        this.age = age;
    }

    public void print() {
        System.out.println("Animal: " + name + " (" + species + ")");
        System.out.println("Condition: " + condition);
        System.out.println("Age: " + age);
        System.out.println("Price: " + price);
    }
}
