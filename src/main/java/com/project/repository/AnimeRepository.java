package com.project.repository;

import com.project.entity.Anime;

import jakarta.persistence.EntityManagerFactory;

public class AnimeRepository extends JpaRepository<Anime, Integer> {
    public AnimeRepository(EntityManagerFactory emf) {
        super(emf);
    }
}

/*
 * Anime has: Studio, Season
 */