package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int id;
    private int fromAccountId;
    private int toAccountId;
    private BigDecimal amount;
    private int typeId;
    private int statusId;

    public String transferToString() {
        //use a switch
        String statusName = null;
        String transferType = null;
            if (statusId == 1) {
                statusName = "PENDING";
            }
            if (statusId == 2) {
                statusName = "APPROVED";
            }
            if (statusId == 3) {
                statusName = "REJECTED";
            }
            if (typeId == 1) {
                transferType = "REQUESTED";
            }
            if (typeId == 2) {
                transferType = "SENT";
            }
            return id +  "  $" + amount + "  " + transferType + "  " + statusName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
