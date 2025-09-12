package com.project.auth;


import java.util.Optional;

import com.project.entity.Account;
import com.project.model.Session;
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

public class LoginViewModel {
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty message = new SimpleStringProperty("");
    private final BooleanProperty loginDisabled = new SimpleBooleanProperty(true);
    private Session session = Session.getInstance();
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
        String user = username.get().trim();
        String pass = password.get().trim();
        Optional<Account> account = authService.login(user, pass);
        if (!account.isPresent()) {
            message.set("Invalid username or password");
            return;
        }

        message.set("Login successful");
        session.setCurrentAccount(account.get());
        if (account.get().getUserRole() == Account.Role.ADMIN) {
            navigationRequest.set(NavigationEvent.NAVIGATE_TO_ADMIN_DASHBOARD);
            message.set("");
            return;
        }else{
            navigationRequest.set(NavigationEvent.NAVIGATE_TO_USER_DASHBOARD);
            message.set("");
            return;
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
