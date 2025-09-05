package com.project.service;

import com.project.entity.Account;
import com.project.navigation.View;
import com.project.repository.AccountRepository;

public class AuthService {
    private final AccountRepository accountRepository;

    public AuthService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;

    }

    public View login(String username, String password){
        Account account = accountRepository.login(username, password);
        if(account != null){
            if(Account.Role.USER.equals(account.getUserRole())){
                return View.USER_DASHBOARD;
            }

            if(Account.Role.ADMIN.equals(account.getUserRole())){
                return View.ADMIN_DASHBOARD;
            }
        }

        return View.LOGIN;
    }

    public boolean authenticate(String username, String password) {
        return "admin".equals(username) && "1234".equals(password);
    }

    public void logout(){

    }
}
