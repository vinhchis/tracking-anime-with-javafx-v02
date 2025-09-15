package com.project.repository;

import java.util.List;

import com.project.entity.Anime;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class AnimeRepository extends JpaRepository<Anime, Integer> {
    public AnimeRepository(EntityManagerFactory emf) {
        super(emf);
    }

    public List<Anime> findAllWithRelations(){
          EntityManager em = emf.createEntityManager();
        try {
            // need  to two joints
            List<Anime> animes = em.createQuery(
                    "SELECT a FROM Anime a LEFT JOIN FETCH a.studio LEFT JOIN FETCH a.season", Anime.class)
                    .getResultList();
            return animes;
        } finally {
            em.close();
        }
    }
}

/*
 * Anime has: Studio, Season
 */