package com.project.controller;

import com.project.entity.User;
import com.project.navigation.SceneManager;
import com.project.service.UserService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UserController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> saveUser());
    }


    private UserService userService = new UserService();

    private void saveUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        User user = new User(name, email);

        long id = userService.savaUser(user).getId();
        System.out.println("Saved userId: " + id);
    }

}
