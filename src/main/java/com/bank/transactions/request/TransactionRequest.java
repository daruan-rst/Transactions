package com.bank.transactions.request;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionType;
import com.bank.transactions.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionRequest {

    private int id;

    private int currentAcc;

    private int targetAcc;

    private BigDecimal money;

    private TransactionType transactionType;

    public Transaction convert(AccountRepository accountRepository){
        Optional<Account> accountOptional = accountRepository.findById(currentAcc);
        Account currentAccount = accountOptional.get();
        return new Transaction(id,currentAccount,targetAcc,money,transactionType);
    }

}
