package com.project.auth;

import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.service.AuthService;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel implements SceneManaged {
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty message = new SimpleStringProperty("");
    private final BooleanProperty loginDisabled = new SimpleBooleanProperty(true);

    private final AuthService authService = new AuthService();
    private SceneManager sceneManager;


    public LoginViewModel() {
        loginDisabled.bind(Bindings.createBooleanBinding(
            () -> username.get().trim().isEmpty() || password.get().trim().isEmpty(),
            username,
            password
        ));
    }

    @Override
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void login() {
        boolean isAuthenticated = authService.authenticate(username.get(), password.get());

        if (isAuthenticated) {
            message.set("Login Successfully!!! " + username.get());
            // sceneManager.switchTo(View.USER_DASHBOARD);
        } else {
            message.set("Your username or password is't correct.\nPlease, enter again!!!");
        }
    }

    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty messageProperty() { return message; }
    public BooleanProperty loginDisabledProperty() { return loginDisabled; }




}
