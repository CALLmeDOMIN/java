package com.example.model;

import jakarta.persistence.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Rating")
@Getter
@Setter
public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int value;

    @Column
    private String comment;

    @Column(nullable = false)
    private LocalDateTime ratingDate;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private AnimalShelter shelter;

    public Rating() {
        this.ratingDate = LocalDateTime.now();
    }

    public Rating(int value, AnimalShelter shelter, String comment) {
        this();
        this.value = value;
        this.shelter = shelter;
        this.comment = comment;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}