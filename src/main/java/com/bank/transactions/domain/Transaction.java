package com.bank.transactions.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transaction {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "current_acc", referencedColumnName = "account_id")
    private Account currentAcc;

    private int targetAcc;

    private BigDecimal money;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}
