package com.project.auth;

import java.util.function.Consumer;

import com.project.navigation.NavigationEvent;
import com.project.navigation.SceneManaged;
import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.repository.AccountRepository;
import com.project.service.AuthService;
import com.project.util.JpaUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel {
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty message = new SimpleStringProperty("");
    private final BooleanProperty loginDisabled = new SimpleBooleanProperty(true);

    private final AuthService authService;
    private final ObjectProperty<NavigationEvent> navigationRequest = new SimpleObjectProperty<>(null);

    public ObjectProperty<NavigationEvent> navigationRequestProperty() {
        return navigationRequest;
    }

    public LoginViewModel() {
        this.authService = new AuthService(new AccountRepository(JpaUtil.getEntityManagerFactory()));

        // binding
        loginDisabled.bind(Bindings.createBooleanBinding(
                () -> username.get().trim().isEmpty() || password.get().trim().isEmpty(),
                username,
                password));
    }

    public void login() {
        boolean isAuthenticated = authService.authenticate(username.get(), password.get());
        if (isAuthenticated) {
            navigationRequest.setValue(NavigationEvent.LOGIN_SUCCESS);
        } else {
            navigationRequest.setValue(NavigationEvent.NAVIGATE_TO_REGISTER);
        }
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public BooleanProperty loginDisabledProperty() {
        return loginDisabled;
    }

}
