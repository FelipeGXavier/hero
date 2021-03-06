package com.example.demo.walking.application.usecases;

import com.example.demo.walking.infra.data.CreateWalkRequest;
import com.example.demo.walking.domain.entity.Walking;
import com.example.demo.walking.domain.usecase.CreateWalkingUseCase;
import com.example.demo.walking.infra.repository.PetRepository;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateWalking implements CreateWalkingUseCase {

    private final PetRepository petRepository;
    private final WalkingRepository walkingRepository;

    @Autowired
    public CreateWalking(PetRepository petRepository, WalkingRepository walkingRepository) {
        this.petRepository = petRepository;
        this.walkingRepository = walkingRepository;
    }

    @Override
    public Walking create(CreateWalkRequest request) {
        var walking = request.toEntity(this.petRepository);
        return this.walkingRepository.save(walking);
    }
}
