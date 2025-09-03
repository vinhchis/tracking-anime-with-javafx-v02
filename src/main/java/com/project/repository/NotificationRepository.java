package com.project.repository;

import com.project.entity.Notification;

import jakarta.persistence.EntityManagerFactory;

public class NotificationRepository extends JpaRepository<Notification, Integer> {
    public NotificationRepository(EntityManagerFactory emf) {
        super(emf);
    }
}
