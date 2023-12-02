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

    private final RestTemplate restTemplate = new RestTemplate();
    private final AccountService accountService = new AccountService();
    private String authToken = null;

    public Transfer create(Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(newTransfer);
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
                    restTemplate.exchange(createPendingTransfersUrl(accountId), HttpMethod.GET, makeAuthEntity(), Transfer[].class);
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
                    restTemplate.exchange(createCompletedTransfersUrl(accountId) , HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
