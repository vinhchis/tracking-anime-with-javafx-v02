package com.project.repository;

import com.project.entity.Tracking;

import jakarta.persistence.EntityManagerFactory;

public class TrackingRepository extends JpaRepository<Tracking, Integer> {
    public TrackingRepository(EntityManagerFactory emf) {
        super(emf);
    }
}
