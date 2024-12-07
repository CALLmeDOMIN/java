package com.example.dev.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalRequest {
    private String name;
    private String species;
    private int age;
    private String condition;
    private Long shelterId;
}