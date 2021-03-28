package com.example.demo.walking.domain.usecase;

import com.example.demo.walking.domain.entity.Caregiver;

public interface AssignCaregiverToWalkingUseCase {

    void acceptWalking(Caregiver caregiver, Long walkingId);
}
