package com.project.service;

public class AuthService {
    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "1234".equals(password);
    }
}
