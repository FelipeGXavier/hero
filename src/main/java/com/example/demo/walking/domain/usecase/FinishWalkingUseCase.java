package com.example.demo.walking.domain.usecase;

import com.example.demo.walking.domain.entity.Caregiver;

public interface FinishWalkingUseCase {

    void finish(Caregiver caregiver, Long id);
}
