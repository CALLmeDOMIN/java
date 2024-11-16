package com.example.animalshelter;

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

    public double getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPrice() {
        return price;
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
}
