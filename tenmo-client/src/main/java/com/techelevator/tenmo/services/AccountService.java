package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.web.client.RestTemplate;

public class AccountService {

    public String API_BASE_URL = "http://localhost:8080/accounts/";
    private final RestTemplate restTemplate = new RestTemplate();

    public Account getAccountById(int accountId) {
        Account account = null;
        account = restTemplate.getForObject(API_BASE_URL + accountId, Account.class);
        return account;


    }
//    public Account getAccountByUserId(int userId) {
//        Account account = null;
//        account = restTemplate.getForObject(API_BASE_URL + userId, Account.class);
//        return account;
//    }



}
