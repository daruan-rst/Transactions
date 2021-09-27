package com.bank.transactions.controller;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionType;
import com.bank.transactions.exceptions.InvalidTimeException;
import com.bank.transactions.exceptions.NotEnoughMoneyException;
import com.bank.transactions.exceptions.OverTheLimitException;
import com.bank.transactions.repository.AccountRepository;
import com.bank.transactions.repository.TransactionRepository;
import com.bank.transactions.response.TransactionResponse;
import com.bank.transactions.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Calendar now = Calendar.getInstance();
    private final int today = now.get(Calendar.DAY_OF_WEEK);
    private final int hour = now.get(Calendar.HOUR_OF_DAY);
    private final TransactionService transactionService;


    @GetMapping("/find-all")
    public ResponseEntity<List<TransactionResponse>> findAll(){
        var transactions = transactionRepository.findAll();
        return ResponseEntity.ok().body(TransactionResponse.convert(transactions));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestParam int targetAccountId,
            @RequestParam BigDecimal depositAmmount,
            UriComponentsBuilder uriComponentsBuilder){
        Transaction thisTransaction = transactionService.deposit(targetAccountId, depositAmmount);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestParam int currentAccountId,
            @RequestParam BigDecimal withdrawAmmount,
            UriComponentsBuilder uriComponentsBuilder){

        Transaction thisTransaction = transactionService.withdraw(currentAccountId,withdrawAmmount);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }

    @PostMapping("/transfer_pix")
    public ResponseEntity<TransactionResponse> transferPix(
            @RequestParam int currentAccountId,
            @RequestParam int targetAccountId,
            @RequestParam BigDecimal transferAmmount,
            UriComponentsBuilder uriComponentsBuilder){
        Transaction thisTransaction = transactionService
                .transfer(currentAccountId, targetAccountId, transferAmmount, TransactionType.TRANSFER_PIX);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }

    @PostMapping("/transfer_ted")
    public ResponseEntity<TransactionResponse> transferTed(
            @RequestParam int currentAccountId,
            @RequestParam int targetAccountId,
            @RequestParam BigDecimal transferAmmount,
            UriComponentsBuilder uriComponentsBuilder){

        transactionService.tedConditions();

        Transaction thisTransaction = transactionService
                .transfer(currentAccountId, targetAccountId, transferAmmount, TransactionType.TRANSFER_TED);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }

    @PostMapping("/transfer_doc")
    public ResponseEntity<TransactionResponse> transferDoc(
            @RequestParam int currentAccountId,
            @RequestParam int targetAccountId,
            @RequestParam BigDecimal transferAmmount,
            UriComponentsBuilder uriComponentsBuilder){

        transactionService.docConditions(transferAmmount);

        Transaction thisTransaction = transactionService
                .transfer(currentAccountId, targetAccountId, transferAmmount, TransactionType.TRANSFER_DOC);

        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }



}
