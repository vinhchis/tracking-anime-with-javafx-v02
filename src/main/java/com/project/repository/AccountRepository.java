package com.project.repository;

import java.util.Optional;

import com.project.entity.Account;
import com.project.entity.Account.Role;
import com.project.util.HashUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class AccountRepository extends JpaRepository<Account, Integer> {
    public AccountRepository(EntityManagerFactory emf) {
        super(emf);
    }

    public Account login(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            Account account = em.createQuery(
                    "SELECT a FROM Account a WHERE a.username = :u", Account.class)
                    .setParameter("u", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (account == null)
                return null;

            String storedHash = account.getHashedPassword();
            boolean verified = false;
            if (storedHash != null && password != null) {
                try {
                    verified = HashUtil.decode(password, storedHash);
                } catch (Exception e) {
                    verified = false;
                }
            }

            return verified ? account : null;
        } finally {
            em.close();
        }
    }

    public Optional<Account> findbyUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            Account account = em.createQuery(
                    "SELECT a FROM Account a WHERE a.username = :u", Account.class)
                    .setParameter("u", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(account);
        } finally {
            em.close();
        }
    }
}
