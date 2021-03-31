package com.example.demo.walking.application.usecases;

import com.example.demo.common.EntityNotFoundException;
import com.example.demo.walking.domain.entity.Caregiver;
import com.example.demo.walking.domain.usecase.AssignCaregiverToWalkingUseCase;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcceptWalking implements AssignCaregiverToWalkingUseCase {

    private final WalkingRepository walkingRepository;
    private final Logger logger = LoggerFactory.getLogger(AcceptWalking.class);

    @Autowired
    public AcceptWalking(WalkingRepository walkingRepository) {
        this.walkingRepository = walkingRepository;
    }

    @Override
    public void acceptWalking(Caregiver caregiver, Long walkingId) {
        var walking =
                this.walkingRepository
                        .findById(walkingId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Walking not found, was not possible to accept this walk"));
        walking.acceptWalk(caregiver);
    }
}
