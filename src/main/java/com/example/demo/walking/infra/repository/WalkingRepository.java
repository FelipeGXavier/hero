package com.example.demo.walking.infra.repository;

import com.example.demo.walking.domain.entity.Walking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkingRepository extends JpaRepository<Walking, Long> {

    @Query("select w from Walking w where w.scheduledDate >= CURRENT_DATE")
    Page<Walking> findAllGreaterThanToday(Pageable pageable);
}
