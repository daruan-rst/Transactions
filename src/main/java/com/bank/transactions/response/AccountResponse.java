package com.bank.transactions.response;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.AccountType;

import java.math.BigDecimal;

public class AccountResponse {

    private int accountId;

    private String userId;

    private String email;

    private BigDecimal money;

    private AccountType accountType;

    public AccountResponse (Account acc){
        this.accountId = acc.getAccountId();
        this.userId = acc.getUserId();
        this.email = acc.getEmail();
        this.money = acc.getMoney();
        this.accountType = acc.getAccountType();
    }
}
