package com.project.model;

import com.project.entity.Account;


public class Session {
    private static Account currentAccount;
    public static Account getCurrentAccount() {
        return currentAccount;
    }
    public static void setCurrentAccount(Account currentAccount) {
        Session.currentAccount = currentAccount;
    }
    public void clear() {
        currentAccount = null;
    }
}
