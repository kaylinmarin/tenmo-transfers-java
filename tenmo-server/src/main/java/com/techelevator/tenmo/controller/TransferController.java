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

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This transfer does not exist.");
        }
        return transfer;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }
    @RequestMapping(path = "{transferId}/{status}", method = RequestMethod.PUT)
    public Transfer updateTransferStatus(@PathVariable int transferId, @PathVariable String status) {
        Transfer existingTransfer = transferDao.getTransferById(transferId);
        if (existingTransfer == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(existingTransfer.getStatusId() != 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer is not in 'pending' status.");

        if(!status.equals("approve") && !status.equals("reject"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfers can only have their statuses changed to 'approve' or 'reject'");

        transferDao.updateTransferStatus(transferId, status.equals("approve") ? 2 : 3);

        return transferDao.getTransferById(transferId);
    }


}
