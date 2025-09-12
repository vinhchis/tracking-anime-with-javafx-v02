package com.project.user;

import com.project.entity.Account;
import com.project.model.Session;
import com.project.navigation.NavigationEvent;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class UserDashboardViewModel {
    private final ObjectProperty<Account> currentUser = new SimpleObjectProperty<>(null);
    private final BooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    private final ObjectProperty<NavigationEvent> navigationRequest = new SimpleObjectProperty<>(null);
    private Session session = Session.getInstance();

    public UserDashboardViewModel(){
        currentUser.bind(session.currentAccountProperty());
        isLoggedIn.bind(session.loggedInProperty());

    }

    public void register(){
        navigationRequest.setValue(NavigationEvent.NAVIGATE_TO_REGISTER);
    }

    public void logout() {
        session.clear();
        navigationRequest.setValue(NavigationEvent.NAVIGATE_TO_USER_DASHBOARD);
    }

    public void login() {
        navigationRequest.setValue(NavigationEvent.NAVIGATE_TO_LOGIN);;
    }

    public ObjectProperty<Account> getCurrentUser() {
        return currentUser;
    }

    public BooleanProperty getIsLoggedIn() {
        return isLoggedIn;
    }

    public ObjectProperty<NavigationEvent> navigationRequestProperty() {
        return navigationRequest;
    }


}
