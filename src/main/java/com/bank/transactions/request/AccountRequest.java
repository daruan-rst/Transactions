package com.bank.transactions.request;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.AccountType;

import java.math.BigDecimal;

public class AccountRequest {

    private int accountId;

    private String userId;

    private String email;

    private BigDecimal money;

    private AccountType accountType;

    public Account convert(){
        return new Account(this.accountId, this.userId, this.email, this.money, this.accountType);
    }
}
