package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    public String API_BASE_URL = "http://localhost:8080/transfers/";
    private final RestTemplate restTemplate = new RestTemplate();

    public Transfer createTransfer(Transfer transfer) {
        ResponseEntity<Transfer> newTransfer = restTemplate.postForEntity(API_BASE_URL, transfer, Transfer.class);
        return newTransfer.getBody();
    }
}
