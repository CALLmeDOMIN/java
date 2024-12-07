package com.example.dev.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Rating")
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int value;
    private String comment;
    private LocalDateTime ratingdate;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @JsonBackReference
    private AnimalShelter shelter;

    public Rating() {
    }

    public Rating(int value, String comment) {
        this.value = value;
        this.comment = comment;
        this.ratingdate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Rating))
            return false;

        Rating rating = (Rating) o;

        return this.value == rating.value && this.comment.equals(rating.comment)
                && this.ratingdate.equals(rating.ratingdate);
    }

    @Override
    public int hashCode() {
        return this.value + this.comment.hashCode() + this.ratingdate.hashCode();
    }

    @Override
    public String toString() {
        return "Rating{" + "id=" + this.id + ", value=" + this.value + ", comment='" + this.comment + '\''
                + ", ratingDate=" + this.ratingdate + '}';
    }
}