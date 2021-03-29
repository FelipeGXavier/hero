package com.example.demo.walking.application.usecases;

import com.example.demo.common.EntityNotFoundException;
import com.example.demo.common.PermissionDeniedException;
import com.example.demo.walking.domain.entity.Caregiver;
import com.example.demo.walking.domain.usecase.FinishWalkingUseCase;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinishWalking implements FinishWalkingUseCase {

    @Autowired private WalkingRepository walkingRepository;

    @Override
    public void finish(Caregiver caregiver, Long id) {
        var walking =
                this.walkingRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Walking not found, was not possible to start the walking"));
        if (walking.getCaregiver() == null || !walking.getCaregiver().equals(caregiver)) {
            throw new PermissionDeniedException("You are not the responsible for this walking");
        }
        walking.finishWalk();
    }
}
