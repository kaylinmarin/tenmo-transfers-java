package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;
    //Will add parameters to constructor when we get to authentication steps.
    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<Transfer> transferList() {
         return transferDao.getTransfers();
    }

    @RequestMapping(path = "{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        //gets transfer by transferId
        Transfer transfer = transferDao.getTransferById(transferId);
        //if it is empty, throw exception (not found)
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This transfer does not exist.");
        }
        //Otherwise return transfer
        return transfer;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }
    @RequestMapping(path = "{transferId}/{status}", method = RequestMethod.PUT)
    public Transfer updateTransferStatus(@PathVariable int transferId, @PathVariable String status) {
        //selects the transfer to update by transferId
        Transfer existingTransfer = transferDao.getTransferById(transferId);
        //if it doesn't exist, throw exception (not found)
        if (existingTransfer == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        //if the transfer status (see database table) is not 1 (pending), throw exception (bad request)
        if(existingTransfer.getStatusId() != 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer is not in 'pending' status.");
        //if status request is not to change to approve or reject, throw exception (bad request) (cannot change to pending)
        if(!status.equals("approve") && !status.equals("reject"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfers can only have their statuses changed to 'approve' or 'reject'");

        Transfer transfer = transferDao.getTransferById(transferId);

        // if status was approve move balance
        if(status.equals("approve")) {
            Account sourceAccount = accountDao.getAccountById(transfer.getFromAccountId());
            Account targetAccount = accountDao.getAccountById(transfer.getToAccountId());

            // if source account doesn't have enough money, reject transfer and bad request
            if(transfer.getAmount().compareTo(sourceAccount.getBalance()) == 1){
                transferDao.updateTransferStatus(transferId, 3);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money in source account to complete transfer.");
            }
            accountDao.setAccountAmount(sourceAccount.getAccount_id(), sourceAccount.getBalance().subtract(transfer.getAmount()));
            accountDao.setAccountAmount(targetAccount.getAccount_id(), targetAccount.getBalance().add(transfer.getAmount()));
        }
        //after passing all if conditions, update the transfer status to 2 or 3 (see database table transfer_status)
        transferDao.updateTransferStatus(transferId, status.equals("approve") ? 2 : 3);
        //return transfer by transferId
        return transferDao.getTransferById(transferId);
    }


}
