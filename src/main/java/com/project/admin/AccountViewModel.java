package com.project.admin;

import com.project.entity.Account;
import com.project.repository.AccountRepository;
import com.project.service.AccountService;
import com.project.service.AuthService;
import com.project.util.JpaUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AccountViewModel {
    private ObservableList<Account> accounts = FXCollections.observableArrayList();
    private ObjectProperty<Account> selectedAccount = new SimpleObjectProperty<>();

    public ObjectProperty<Account> getSelectedAccount() {
        return selectedAccount;
    }

    private StringProperty adminCount = new SimpleStringProperty("0");

    public StringProperty getAdminCount() {
        return adminCount;
    }

    private StringProperty userCount = new SimpleStringProperty("0");

    public StringProperty getUserCount() {
        return userCount;
    }

    private StringProperty totalCount = new SimpleStringProperty("0");

    public StringProperty getTotalCount() {
        return totalCount;
    }

    private AccountService accountService;
    private AuthService authService;

    public AccountViewModel() {
        accountService = new AccountService(new AccountRepository(JpaUtil.getEntityManagerFactory()));
        authService = new AuthService(new AccountRepository(JpaUtil.getEntityManagerFactory()));

        adminCount.set(String.valueOf(accountService.numberOfAdmin()));
        userCount.set(String.valueOf(accountService.numberOfUser()));
        totalCount.set(String.valueOf(accountService.count()));

        accounts.setAll(accountService.accounts());
    }

    // delete from delete button in cell
    public void deleteAccount(Account account) {
        accountService.deleteAccount(account);
        accounts.remove(account);

        setCountLabels();

    }

    private void setCountLabels() {
        adminCount.set(String.valueOf(accountService.numberOfAdmin()));
        userCount.set(String.valueOf(accountService.numberOfUser()));
        totalCount.set(String.valueOf(accountService.count()));
    }

    public ObservableList<Account> getAccounts() {
        return accounts;
    }

    public ObjectProperty<Account> selectedAccountProperty() {
        return selectedAccount;
    }

}
