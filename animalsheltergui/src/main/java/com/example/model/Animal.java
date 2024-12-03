package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;
import java.io.Serializable;

@Entity
@Table(name = "Animal")
@Getter
@Setter
public class Animal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnimalCondition condition;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double weight;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private AnimalShelter shelter;

    public Animal() {
    }

    public Animal(String name, String species, AnimalCondition condition, int age, double weight) {
        this.name = name;
        this.species = species;
        this.condition = condition;
        this.age = age;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Animal animal = (Animal) o;
        return Objects.equals(getId(), animal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
