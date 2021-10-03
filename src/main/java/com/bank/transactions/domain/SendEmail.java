package com.bank.transactions.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
public class SendEmail {

    private String email;
    private String currentAccount;
    private String targetAccount;
    private BigDecimal ammount;
    private String operation;

}
