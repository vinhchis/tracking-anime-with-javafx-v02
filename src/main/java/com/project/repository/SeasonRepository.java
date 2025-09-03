package com.project.repository;

import com.project.entity.Season;
import com.project.model.SeasonId;

import jakarta.persistence.EntityManagerFactory;

public class SeasonRepository extends JpaRepository<Season, SeasonId> {
    public SeasonRepository(EntityManagerFactory emf) {
        super(emf);
    }
}
