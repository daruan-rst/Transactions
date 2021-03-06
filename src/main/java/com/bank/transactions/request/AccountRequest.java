package com.bank.transactions.request;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {

    private int accountId;

    private String userId;

    private String email;

    private BigDecimal money;

    public Account convert(AccountType accountType){
        return new Account(this.accountId, this.userId, "" , this.email, this.money, accountType);
    }
}
