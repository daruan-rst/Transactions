package com.bank.transactions.controller;

import com.bank.transactions.domain.PhoneCreditValue;
import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionType;
import com.bank.transactions.repository.TransactionRepository;
import com.bank.transactions.response.TransactionResponse;
import com.bank.transactions.service.EmailService;
import com.bank.transactions.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final EmailService emailService;


    @GetMapping("/find-all")
    public ResponseEntity<List<TransactionResponse>> findAll(){
        var transactions = transactionRepository.findAll();
        return ResponseEntity.ok().body(TransactionResponse.convert(transactions));
    }

    @GetMapping("/bank-statement")
    public ResponseEntity<List<TransactionResponse>> bankStatement(@RequestParam int accountId){

        return ResponseEntity.ok().body(TransactionResponse.convert(transactionService.bankStatement(accountId)));
    }


    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestParam int targetAccountId,
            @RequestParam BigDecimal depositAmmount,
            UriComponentsBuilder uriComponentsBuilder){
        Transaction thisTransaction = transactionService.deposit(targetAccountId, depositAmmount);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        emailService.sendingTheEmail(thisTransaction);
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
        emailService.sendingTheEmail(thisTransaction);
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }

    @PostMapping("/phone-credit")
    public ResponseEntity<TransactionResponse> phoneCredit(
            @RequestParam int currentAccountId,
            @RequestParam PhoneCreditValue credit,
            UriComponentsBuilder uriComponentsBuilder){

        Transaction thisTransaction = transactionService.phoneCredit(currentAccountId,credit);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        emailService.sendingTheEmail(thisTransaction);
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction));
    }

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
            emailService.sendingTheEmail(thisTransaction);
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
        emailService.sendingTheEmail(thisTransaction);
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
        emailService.sendingTheEmail(thisTransaction);
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }



}
