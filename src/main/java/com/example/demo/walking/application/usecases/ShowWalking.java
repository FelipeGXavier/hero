package com.example.demo.walking.application.usecases;

import com.example.demo.common.EntityNotFoundException;
import com.example.demo.walking.domain.usecase.ShowWalkingUseCase;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowWalking implements ShowWalkingUseCase {

    private final WalkingRepository walkingRepository;

    @Autowired
    public ShowWalking(WalkingRepository walkingRepository) {
        this.walkingRepository = walkingRepository;
    }

    @Override
    public int show(Long id) {
        var walking =
                this.walkingRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Walking not found, was not possible to get real duration"));

        return walking.getRealDuration();
    }
}
