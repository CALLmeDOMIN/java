package com.example.dev.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Animal")
@Getter
@Setter
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private int age;

    @Enumerated(EnumType.STRING)
    private AnimalCondition condition;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @JsonBackReference
    private AnimalShelter shelter;

    public Animal() {
    }

    public Animal(String name, String species, int age, AnimalCondition condition) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Animal))
            return false;

        Animal animal = (Animal) o;

        return Objects.equals(this.id, animal.id) && Objects.equals(this.name, animal.name)
                && Objects.equals(this.species, animal.species) && Objects.equals(this.age, animal.age)
                && this.condition == animal.condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.species, this.age, this.condition);
    }

    @Override
    public String toString() {
        return "Animal{" + "id=" + this.id + ", name='" + this.name + '\'' + ", species='" + this.species + '\''
                + ", age=" + this.age + ", condition=" + this.condition + '}';
    }
}