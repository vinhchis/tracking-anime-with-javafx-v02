package com.project.repository;

import com.project.entity.Account;

import jakarta.persistence.EntityManagerFactory;

public class AccountRepository extends JpaRepository<Account, Integer> {
    public AccountRepository(EntityManagerFactory emf) {
        super(emf);
    }
}
