package com.project.service;

import java.util.Optional;

import com.project.entity.Account;
import com.project.model.Session;
import com.project.repository.AccountRepository;
import com.project.util.HashUtil;

public class AuthService {
    private final AccountRepository accountRepository;

    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

    }

    public Optional<Account> login(String username, String password) {
        Account account = accountRepository.login(username, password);
        if (account != null) {
            Session.setCurrentAccount(account);
            return Optional.of(account);
        }

        return Optional.empty();
    }

    public Account register(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        try {
            String hashedPass = HashUtil.encode(password);
            account.setHashedPassword(hashedPass);
        } catch (Exception e) {
            System.err.println("Can't generate hashedPass");
        }
        account.setUserRole(Account.Role.USER);// default role
        return accountRepository.save(account);
    }

    public boolean checkAccountExisted(String username){
        return accountRepository.findbyUsername(username).isPresent();
    }

    public void logout() {
        Session.setCurrentAccount(null);
    }
}
