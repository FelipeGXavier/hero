package com.example.demo.walking.domain.usecase;

import com.example.demo.walking.infra.data.CreateWalkRequest;
import com.example.demo.walking.domain.entity.Walking;

public interface CreateWalkingUseCase {

    Walking create(CreateWalkRequest request);
}
