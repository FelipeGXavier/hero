package com.example.demo.walking.infra.repository;

import com.example.demo.walking.domain.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {}
