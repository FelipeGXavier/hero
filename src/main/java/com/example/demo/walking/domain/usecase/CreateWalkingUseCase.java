package com.example.demo.walking.domain.usecase;

import com.example.demo.walking.adapters.CreateWalkRequest;
import com.example.demo.walking.domain.entity.Walking;

public interface CreateWalkingUseCase {

    Walking create(CreateWalkRequest request);
}
