package com.example.demo.walking.domain.usecase;

import com.example.demo.walking.domain.entity.Caregiver;

public interface StartWalkingUseCase {

    void startWalk(Caregiver caregiver, Long id);
}
