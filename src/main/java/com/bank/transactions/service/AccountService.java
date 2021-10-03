package com.bank.transactions.service;


import com.bank.transactions.domain.Account;
import com.bank.transactions.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public String balance(int accountId){
        Optional<Account> accounts = accountRepository.findById(accountId);
        Account account = accounts.get();
        BigDecimal balance = account.getMoney();
        if (balance.compareTo(BigDecimal.ZERO) == -1){
            balance = balance.multiply(new BigDecimal("1.01"));
            account.setMoney(balance);
            accountRepository.save(account);
        }
        return "Seu saldo bancário é de R$:" + balance;
    }
}
