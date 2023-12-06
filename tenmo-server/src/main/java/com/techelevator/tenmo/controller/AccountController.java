package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/accounts")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private TransferDao transferDao;


    public AccountController(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
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

    //ADDED FOR RETURNING A BALANCE
    @RequestMapping(path = "/accountByUserId/{userId}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int userId) {
        Account account = accountDao.getAccountByUserId(userId);

        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist");

        } else {
            return accountDao.getAccountByUserId(userId);
        }
    }

    // get pending transfers
    // /accounts/{accountId}/transfers/pending
    @RequestMapping(path = "{accountId}/transfers/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable int accountId) {
        List<Transfer> transfers = transferDao.getPendingTransfersByAccountId(accountId);
        return transfers;
    }

    // get completed transfers
    // /accounts/{accountId}/transfers/completed
    @RequestMapping(path = "{accountId}/transfers/completed", method = RequestMethod.GET)
    public List<Transfer> getCompletedTransfers(@PathVariable int accountId) {
        List<Transfer> transfers = transferDao.getCompletedTransfersByAccountId(accountId);
        return transfers;
    }
}

