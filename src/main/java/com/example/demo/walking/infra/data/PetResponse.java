package com.example.demo.walking.infra.data;

import lombok.Builder;

@Builder
public class PetResponse {

    private final Long id;
    private final String name;
    private final String breed;
}
