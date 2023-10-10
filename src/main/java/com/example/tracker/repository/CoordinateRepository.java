package com.example.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tracker.entity.CoordinateEntity;

@Repository
public interface CoordinateRepository extends JpaRepository<CoordinateEntity, Long> {
    
}
