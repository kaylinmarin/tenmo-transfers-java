package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public Transfer getTransferById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                return mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return null;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_status_id, transfer_type_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id ";

        int newTransferId = jdbcTemplate.queryForObject(sql, int.class,
                transfer.getStatusId(),
                transfer.getTypeId(),
                transfer.getFromAccountId(),
                transfer.getToAccountId(),
                transfer.getAmount());
        transfer.setId(newTransferId);
        return transfer;
    }

    @Override
    public boolean updateTransferStatus(int transferId, int transferStatusId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? " +
                "WHERE transfer_id = ? ";
        int numberOfRows = jdbcTemplate.update(sql,  transferStatusId, transferId);
        return numberOfRows > 0;
    }

    public List<Transfer> getTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet sr) {
        Transfer transfer = new Transfer();
        transfer.setId(sr.getInt("transfer_id"));
        transfer.setStatusId(sr.getInt("transfer_status_id"));
        transfer.setTypeId(sr.getInt("transfer_type_id"));
        transfer.setFromUserId(sr.getInt("account_from"));
        transfer.setToUserId(sr.getInt("account_to"));
        transfer.setAmount(sr.getBigDecimal("amount"));
        return transfer;

    }

}