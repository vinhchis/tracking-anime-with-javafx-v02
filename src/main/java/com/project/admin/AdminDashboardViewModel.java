package com.project.admin;

import com.project.model.Session;
import com.project.navigation.NavigationEvent;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminDashboardViewModel {
    private final StringProperty usernameProperty = new SimpleStringProperty("");
    private final ObjectProperty<NavigationEvent> navigationRequest = new SimpleObjectProperty<>(null);
    private Session session = Session.getInstance();

    public ObjectProperty<NavigationEvent> navigationRequestProperty() {
        return navigationRequest;
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public AdminDashboardViewModel() {
        String username = session.getCurrentAccount() != null ? session.getCurrentAccount().getUsername() : "admin";
        usernameProperty.setValue("Hello Mr/Mrs. " + username);
    }

    public void logout() {
        session.clear();
        navigationRequest.setValue(NavigationEvent.NAVIGATE_TO_LOGIN);
    }

}
