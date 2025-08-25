package com.project.service;

import com.project.entity.User;
import com.project.repository.UserRepository;
import com.project.util.JpaUtil;

import jakarta.persistence.EntityManagerFactory;

public class UserService {
    EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();

    UserRepository userRepository = new UserRepository(emf);

    public User savaUser(User user){
       return userRepository.save(user);
    }

    public boolean authenticate(String user, String password){
        return true;
    }
}
