package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferDao transferDao;
    //Will add parameters to constructor when we get to authentication steps.
    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
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
        //after passing all if conditions, update the transfer status to 2 or 3 (see database table transfer_status)
        transferDao.updateTransferStatus(transferId, status.equals("approve") ? 2 : 3);
        //return transfer by transferId
        return transferDao.getTransferById(transferId);
    }


}
