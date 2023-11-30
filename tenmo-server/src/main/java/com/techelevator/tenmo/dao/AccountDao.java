package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;

import java.util.List;


public interface AccountDao {

    Account getAccountById(int account_id);

    Account getAccountByUserId(int user_id);
    List<Account> getAccounts();


}
