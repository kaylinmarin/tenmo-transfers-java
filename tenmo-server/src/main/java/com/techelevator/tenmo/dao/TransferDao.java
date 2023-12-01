package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface TransferDao {

    Transfer getTransferById(int transferId);

    Transfer createTransfer(Transfer transfer);

    boolean updateTransferStatus(int transferId, int transferStatusId);
    List<Transfer> getTransfers();

}