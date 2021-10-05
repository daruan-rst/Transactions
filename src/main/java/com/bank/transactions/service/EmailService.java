package com.bank.transactions.service;

import com.bank.transactions.client.SendEmail;
import com.bank.transactions.domain.Transaction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class EmailService {

    public static SendEmail makeEmail(Transaction transaction){
        return new SendEmail(transaction.getCurrentAcc().getEmail(),
                String.valueOf(transaction.getCurrentAcc().getAccountId()),
                String.valueOf(transaction.getTargetAcc()),
                transaction.getMoney(),
                String.valueOf(transaction.getTransactionType()));}
}


