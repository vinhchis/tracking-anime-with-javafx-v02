package com.project.service;

import java.util.List;

import com.project.entity.Account;
import com.project.repository.AccountRepository;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> accounts(){
        return accountRepository.findAll();
    }

    public void deleteAccount(Account account){
        accountRepository.delete(account);
    }

    public int count(){
        return (int) accountRepository.count();
    }

    public int numberOfAdmin(){
        return (int) accounts().stream().filter(acc -> acc.getUserRole() == Account.Role.ADMIN).count();
    }

    public int numberOfUser(){
        return (int) accounts().stream().filter(acc -> acc.getUserRole() == Account.Role.USER).count();
    }
}
