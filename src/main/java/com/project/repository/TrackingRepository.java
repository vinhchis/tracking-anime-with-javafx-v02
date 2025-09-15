package com.project.repository;

import java.util.List;
import java.util.Optional;

import com.project.entity.Tracking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class TrackingRepository extends JpaRepository<Tracking, Integer> {
    public TrackingRepository(EntityManagerFactory emf) {
        super(emf);
    }

    public Optional<Tracking> findByAccountIdAndAnimeId(int accountId, int animeId) {
        EntityManager em = emf.createEntityManager();
        try {
            Tracking tracking = em.createQuery(
                    "SELECT t FROM Tracking t WHERE t.account.id = :aId AND t.anime.id = :anId", Tracking.class)
                    .setParameter("aId", accountId)
                    .setParameter("anId", animeId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(tracking);
        } finally {
            em.close();
        }
    }

    // public List<Tracking> findAllWithRelations(int accountId){
    // EntityManager em = emf.createEntityManager();
    // try {
    // String sql1 = "SELECT t FROM Tracking t LEFT JOIN FETCH t.account WHERE
    // t.account.id = :aId";
    // TypedQuery<Tracking> query1 = em.createQuery(sql1, Tracking.class);
    // query1.setParameter("aId", accountId);

    // List<Tracking> trackingResult = query1.getResultList();

    // String sql2 = "SELECT t FROM Tracking t LEFT JOIN FETCH t.anime WHERE
    // t.account.id = :aId";
    // TypedQuery<Tracking> query2 = em.createQuery(sql2, Tracking.class);
    // query2.setParameter("aId", accountId);

    // trackingResult = query2.getResultList();

    // return trackingResult;
    // } finally {
    // em.close();
    // }
    // }

    public List<Tracking> findAllByAccountId(int accountId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Tracking> trackings = em.createQuery(
                    "SELECT t FROM Tracking t " +
                            "LEFT JOIN FETCH t.account " +
                            "LEFT JOIN FETCH t.anime a " +
                            "LEFT JOIN FETCH a.studio " +
                            "LEFT JOIN FETCH a.season " +
                            "WHERE t.account.id = :aId",
                    Tracking.class)
                    .setParameter("aId", accountId)
                    .getResultList();
            return trackings;
        } finally {
            em.close();
        }
    }
}
/*
 * Tracking need : Anime, Account
 */
