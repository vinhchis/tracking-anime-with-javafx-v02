package com.project.model;

import com.project.entity.Account;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.BooleanProperty;

public final class Session {
    private static final Session INSTANCE = new Session();

    private final ObjectProperty<Account> currentAccount = new SimpleObjectProperty<>(null);
    private final BooleanProperty loggedIn = new SimpleBooleanProperty(false);

    private Session() {
        loggedIn.bind(currentAccount.isNotNull());
    }

    public static Session getInstance() {
        return INSTANCE;
    }

    public ObjectProperty<Account> currentAccountProperty() {
        return currentAccount;
    }

    public Account getCurrentAccount() {
        return currentAccount.get();
    }

    public void setCurrentAccount(Account account) {
        currentAccount.set(account);
    }

    public void clear() {
        currentAccount.set(null);
    }

    public BooleanProperty loggedInProperty() {
        return loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn.get();
    }
}
