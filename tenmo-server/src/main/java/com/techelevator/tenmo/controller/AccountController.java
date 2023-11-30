package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/accounts")
//@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    //list all accounts
    @RequestMapping(method = RequestMethod.GET)
    public List<Account> accountList() {
        List<Account> accounts = accountDao.getAccounts();
        return accounts;
    }
    @RequestMapping(path = "{accountId}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable int accountId) {
        Account account = accountDao.getAccountById(accountId);
        //gets account by id, if it is empty, throw exception (not found)
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This account does not exist.");
        }
        //else return the account selected
        return account;
    }

}

