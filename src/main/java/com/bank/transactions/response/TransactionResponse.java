package com.bank.transactions.response;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class TransactionResponse {
    private int id;

    private Account currentAcc;

    private int targetAcc;

    private BigDecimal money;

    private TransactionType transactionType;

    public TransactionResponse (Transaction transaction){
        this.id = transaction.getId();
        this.currentAcc = transaction.getCurrentAcc();
        this.targetAcc = transaction.getTargetAcc();
        this.money = transaction.getMoney();
        this.transactionType = transaction.getTransactionType();
    }

    public static List<TransactionResponse> convert(List<Transaction> transactions){
        return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

}
