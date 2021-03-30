package com.example.demo.walking.adapters;

import lombok.Builder;

@Builder
public class PetResponse {

    private final Long id;
    private final String name;
    private final String breed;
}
