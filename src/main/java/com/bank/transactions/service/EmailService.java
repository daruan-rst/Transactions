package com.bank.transactions.service;

import com.bank.transactions.domain.SendEmail;
import com.bank.transactions.domain.Transaction;
import lombok.ToString;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;


@Service
public class EmailService {



    public void sendingTheEmail(Transaction transaction) {
        final String SEND_EMAIL = ("http://localhost:6080/email/send-email");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SendEmail> requestEntity = new HttpEntity<>(makeEmail(transaction), headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(SEND_EMAIL, requestEntity, SendEmail.class);
    }

    private SendEmail makeEmail(Transaction transaction){
        return new SendEmail(transaction.getCurrentAcc().getEmail(),
                String.valueOf(transaction.getCurrentAcc().getAccountId()),
                String.valueOf(transaction.getTargetAcc()),
                transaction.getMoney(),
                String.valueOf(transaction.getTransactionType()));}
}


