package com.project.repository;

import com.project.entity.User;

import jakarta.persistence.EntityManagerFactory;

public class UserRepository extends JpaRepository<User, Long> {

    public UserRepository(EntityManagerFactory emf) {
        super(emf);
    }

}
