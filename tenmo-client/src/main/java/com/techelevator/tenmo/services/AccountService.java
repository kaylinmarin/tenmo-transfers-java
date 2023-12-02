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
    private String authToken;

    // JILL ADDED THIS
    public Account getAccountByUserId(int userId){
        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(
                    API_URL_Get_Account_By_User + userId, // url
                    HttpMethod.GET, // verb
                    HttpUtilities.createEntity(authToken), // headers and optional body
                    Account.class); // return type (the body will be deserialized into this type)

            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
