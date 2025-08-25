package com.project.util;

import java.util.HashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            Dotenv dotenv = Dotenv.load();

            Map<String, String> properties = new HashMap<>();

            properties.put("jakarta.persistence.jdbc.driver", dotenv.get("DB_DRIVER"));
            properties.put("jakarta.persistence.jdbc.url", dotenv.get("DB_URL"));
            properties.put("jakarta.persistence.jdbc.user", dotenv.get("DB_USER"));
            properties.put("jakarta.persistence.jdbc.password", dotenv.get("DB_PASSWORD"));

            entityManagerFactory = Persistence.createEntityManagerFactory("MyPU", properties);

        } catch (Throwable ex) {
            System.err.println("Lỗi khởi tạo EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public static void shutdown() {
        getEntityManagerFactory().close();
    }
}
