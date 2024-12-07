package com.example.dev.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AnimalShelter")
@Getter
@Setter
public class AnimalShelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer capacity;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Animal> animals = new HashSet<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Rating> ratings = new HashSet<>();

    public AnimalShelter() {
    }

    public AnimalShelter(String name, Integer capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AnimalShelter))
            return false;

        AnimalShelter shelter = (AnimalShelter) o;

        return Objects.equals(this.id, shelter.id) && Objects.equals(this.name, shelter.name)
                && Objects.equals(this.capacity, shelter.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.capacity);
    }

    @Override
    public String toString() {
        return "AnimalShelter{" + "id=" + this.id + ", name='" + this.name + '\'' + ", capacity=" + this.capacity + '}';
    }
}