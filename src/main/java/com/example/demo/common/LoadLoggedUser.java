package com.example.demo.common;

import com.example.demo.walking.domain.entity.Caregiver;
import com.example.demo.walking.infra.repository.CaregiverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LoadLoggedUser {

    @Autowired private CaregiverRepository caregiverRepository;

    /** Simulate logged in user e.g. token based or session */
    public Caregiver loadLoggedUser() {
        var caregivers = this.caregiverRepository.findAll();
        Collections.shuffle(caregivers);
        return caregivers.get(0);
    }
}
