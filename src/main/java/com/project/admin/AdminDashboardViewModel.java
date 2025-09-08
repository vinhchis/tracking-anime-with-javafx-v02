package com.project.admin;

import com.project.model.Session;
import com.project.navigation.NavigationEvent;
import com.project.repository.AccountRepository;
import com.project.service.AuthService;
import com.project.util.JpaUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminDashboardViewModel {
    private final StringProperty usernameProperty = new SimpleStringProperty("");
    private final ObjectProperty<NavigationEvent> navigationRequest = new SimpleObjectProperty<>(null);

    private final AuthService authService;

    public ObjectProperty<NavigationEvent> navigationRequestProperty() {
        return navigationRequest;
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public AdminDashboardViewModel() {
        authService = new AuthService(new AccountRepository(JpaUtil.getEntityManagerFactory()));
        String username = (Session.getCurrentAccount() != null) ? Session.getCurrentAccount().getUsername() : "admin";
        usernameProperty.setValue("Hello Mr/Mrs. " + username);
    }

    public void logout() {
        authService.logout();
        navigationRequest.setValue(NavigationEvent.NAVIGATE_TO_LOGIN);
    }

}
