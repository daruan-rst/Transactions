package com.bank.transactions.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
public class SendEmail {

    private String userName;
    private String email;
    private String currentAccount;
    private String targetAccount;
    private BigDecimal ammount;
    private String operation;

}
