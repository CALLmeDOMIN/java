package com.example.dev.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {
    private int value;
    private String comment;
    private Long shelterId;
}