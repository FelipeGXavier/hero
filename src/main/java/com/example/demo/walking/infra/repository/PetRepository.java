package com.example.demo.walking.infra.repository;

import com.example.demo.walking.domain.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p WHERE p.id in :ids")
    List<Pet> findPetsWhereIn(@Param("ids") List<Long> ids);
}
