package com.project.auth;

import com.project.navigation.NavigationEvent;
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

public class RegisterViewModel {
    StringProperty username = new SimpleStringProperty("");
    StringProperty password = new SimpleStringProperty("");
    StringProperty confirmPassword = new SimpleStringProperty("");
    StringProperty message = new SimpleStringProperty("");
    private final BooleanProperty registerDisabled = new SimpleBooleanProperty(true);

    private final AuthService authService;
    private final ObjectProperty<NavigationEvent> navigationRequest = new SimpleObjectProperty<>(null);

    public RegisterViewModel() {
        authService = new AuthService(new AccountRepository(JpaUtil.getEntityManagerFactory()));

        registerDisabled.bind(Bindings.createBooleanBinding(
                () -> username.get().trim().isEmpty() || password.get().trim().isEmpty()
                        || confirmPassword.get().trim().isEmpty(),
                username, password, confirmPassword));
    }

    public void register() {
        String user = username.get().trim();
        String pass = password.get().trim();
        String confirmPass = confirmPassword.get().trim();

        // regex password neeed
        // - at least 5 characters, at least one digit, one upper case letter,
        if (pass.length() < 5 || !pass.matches(".*\\d.*") || !pass.matches(".*[A-Z].*")) {
            message.set(
                    "Password must be at least 5 characters long, contain at least one digit and one uppercase letter");
            return;
        }

        if (!pass.equals(confirmPass)) {
            message.set("Password and Confirm Password do not match");
            return;
        }

        if (authService.checkAccountExisted(user)) {
            message.set("Username already exists");
            return;
        }

        authService.register(user, pass);
        message.set("Register successful");
        navigationRequest.set(NavigationEvent.NAVIGATE_TO_LOGIN);
    }

    public ObjectProperty<NavigationEvent> navigationRequestProperty() {
        return navigationRequest;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty confirmPasswordProperty() {
        return confirmPassword;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public BooleanProperty registerDisabledProperty() {
        return registerDisabled;
    }

}
