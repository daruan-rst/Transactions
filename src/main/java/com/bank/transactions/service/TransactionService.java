package com.bank.transactions.service;

import com.bank.transactions.domain.Account;

import java.math.BigDecimal;

public class TransactionService {

    public boolean hasEnoughMoney(Account account, BigDecimal subtractMoney){
        return account.getMoney().compareTo(subtractMoney) > 0;
    }
}
