package com.project.repository;

import com.project.entity.Episode;

import jakarta.persistence.EntityManagerFactory;

public class EpisodeRepository extends JpaRepository<Episode, Integer> {
    public EpisodeRepository(EntityManagerFactory emf) {
        super(emf);
    }
}
