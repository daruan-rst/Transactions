package com.bank.transactions.response;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.AccountType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
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

    public static List<AccountResponse> convert(List<Account> accounts){
        return accounts.stream().map(AccountResponse::new).collect(Collectors.toList());
    }
}
