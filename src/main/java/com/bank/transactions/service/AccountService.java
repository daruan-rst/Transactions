package com.bank.transactions.service;


import com.bank.transactions.client.UserClient;
import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.AccountType;
import com.bank.transactions.exceptions.InvalidUserException;
import com.bank.transactions.repository.AccountRepository;
import com.bank.transactions.request.AccountRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserClient userClient;

    public String balance(int accountId){
        String message = "";
        Optional<Account> accounts = accountRepository.findById(accountId);
        Account account = accounts.get();
        BigDecimal balance = account.getMoney();
        if (balance.compareTo(BigDecimal.ZERO) == -1){
            balance = balance.multiply(new BigDecimal("1.01"));
            account.setMoney(balance);
            accountRepository.save(account);
            message = "Cuidado! Você entrou no cheque especial\n";
        }
        return message + "Seu saldo bancário é de R$:" + balance;
    }

    public Account userVerification(AccountRequest accountRequest, AccountType accountType){
        String cpfRequest =  accountRequest.getUserId();
        if (userClient.doesThisUserExist(cpfRequest).equals("UserNotFound")){
            throw new InvalidUserException();
        }
        List<String> userData = Arrays.asList(userClient.doesThisUserExist(cpfRequest).split(","));
        Account account = accountRequest.convert(accountType);
        account.setUserName(userData.get(1));
        account.setUserId(userData.get(0));
        return account;
    }

    public String isThereSuchAccount(int id){
        Optional<Account> optionalAccount =  accountRepository.findAccountByAccountId(id);
        if (optionalAccount.isEmpty()){
            return "NotSuchAccount";
        }else if(optionalAccount.get().getAccountType() == AccountType.POUPANCA){
            return "InvalidAccountType";
        }
        else{
        return String.valueOf(optionalAccount.get().getAccountId());}
    }
}
