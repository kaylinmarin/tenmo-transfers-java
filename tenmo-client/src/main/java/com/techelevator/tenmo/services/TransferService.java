package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    public String API_BASE_TRANSFER = "http://localhost:8080/transfers";

    private String createPendingTransfersUrl(int accountId)
    {
        return "http://localhost:8080/accounts/" + accountId + "/transfers/pending";
    }
    private String createCompletedTransfersUrl(int accountId) {
        return "http://localhost:8080/accounts/" + accountId + "/transfers/completed";
    }
    private String approveTransferUrl(int transferId) {
        return "http://localhost:8080/transfers/" + transferId + "/approve";
    }
    private String rejectedTransferUrl(int transferId) {
        return "http://localhost:8080/transfers/" + transferId + "/reject";
    }

    public TransferService(AccountService accountService) {

        this.accountService = accountService;
    }

    private final RestTemplate restTemplate = new RestTemplate();
    private final AccountService accountService;
    private String authToken;

    public Transfer create(Transfer newTransfer) {
        HttpEntity<Transfer> entity = HttpUtilities.createEntity(authToken, newTransfer);
        Transfer returnedTransfer = null;
        try {
            returnedTransfer = restTemplate.postForObject(API_BASE_TRANSFER, entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }
    public Transfer[] getPendingTransfers(int accountId) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(createPendingTransfersUrl(accountId), HttpMethod.GET, HttpUtilities.createEntity(authToken), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }
    public Transfer[] getCompletedTransfers(int accountId) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(createCompletedTransfersUrl(accountId) , HttpMethod.GET, HttpUtilities.createEntity(authToken), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }
    public boolean approveTransfer(int transferId) {
        try {
            restTemplate.exchange(approveTransferUrl(transferId) , HttpMethod.PUT, HttpUtilities.createEntity(authToken), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean rejectTransfer(int transferId) {
        try {
            restTemplate.exchange(rejectedTransferUrl(transferId) , HttpMethod.PUT, HttpUtilities.createEntity(authToken), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
        return true;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
