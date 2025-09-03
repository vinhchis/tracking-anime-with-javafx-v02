package com.project.repository;

import com.project.entity.Studio;

import jakarta.persistence.EntityManagerFactory;

public class StudioRepository extends JpaRepository<Studio, Integer> {
    public StudioRepository(EntityManagerFactory emf) {
        super(emf);
    }
}
