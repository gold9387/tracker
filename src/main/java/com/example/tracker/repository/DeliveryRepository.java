package com.example.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tracker.entity.DeliveryEntity;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    
}
