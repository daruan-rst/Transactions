package com.bank.transactions.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter

public class Account {

    @Id
    @Column(name = "account_id")
    private int accountId;

    private String userId;

    private String email;

    @Setter
    private BigDecimal money;

    private AccountType accountType;




}