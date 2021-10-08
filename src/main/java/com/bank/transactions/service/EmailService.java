package com.bank.transactions.service;

import com.bank.transactions.client.SendEmail;
import com.bank.transactions.domain.Transaction;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    public static SendEmail makeEmail(Transaction transaction){
        return new SendEmail(transaction.getCurrentAcc().getUserName(),
                transaction.getCurrentAcc().getEmail(),
                String.valueOf(transaction.getCurrentAcc().getAccountId()),
                String.valueOf(transaction.getTargetAcc()),
                transaction.getMoney(),
                String.valueOf(transaction.getTransactionType()));}
}


