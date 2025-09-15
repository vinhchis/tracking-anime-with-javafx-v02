package com.project.repository;

import com.project.entity.Episode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class EpisodeRepository extends JpaRepository<Episode, Integer> {
    public EpisodeRepository(EntityManagerFactory emf) {
        super(emf);
    }

    public short countByAnimeId(int animeId) {
        EntityManager em = emf.createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(e) FROM Episode e WHERE e.anime.id = :anId", Long.class)
                    .setParameter("anId", animeId)
                    .getSingleResult();
            return count.shortValue();
        } finally {
            em.close();
        }
    }
}

/*
 * Episode has : Anime
 */