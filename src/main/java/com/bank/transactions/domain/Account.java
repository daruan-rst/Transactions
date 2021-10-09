package com.bank.transactions.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Setter
    private String userId;

    @Setter
    private String userName;

    private String email;

    @Setter
    private BigDecimal money;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;




}
