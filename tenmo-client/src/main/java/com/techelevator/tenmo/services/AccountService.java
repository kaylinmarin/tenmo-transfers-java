package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountService {

    public String API_BASE_URL = "http://localhost:8080/accounts/";
    public String API_URL_Get_Account_By_User = "http://localhost:8080/accounts/accountByUserId/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Account getAccountById(int accountId) {
        Account account = null;
        account = restTemplate.getForObject(API_BASE_URL + accountId, Account.class);
        return account;

    }


    //JILL ADDED FROM LECTURE TODAY.
    public Account getAccountByUserId_secure(int userId){
        Account account = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);

       ResponseEntity<Account> response = restTemplate.exchange(API_URL_Get_Account_By_User + userId,
                HttpMethod.GET, entity, Account.class);
       account = response.getBody();

        return account;
    }

//    public Account getAccountByUserId(int userId) {
//        Account account = null;
//        account = restTemplate.getForObject(API_URL_Get_Account_By_User + userId, Account.class);
//        return account;
//    }



    // JILL ADDED THIS
    public Account getAccountByUserId(int userId){
        Account account = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Account> response = restTemplate.exchange(API_URL_Get_Account_By_User + userId,
                HttpMethod.GET, entity, Account.class);
        account = response.getBody();

        return account;
    }



}
