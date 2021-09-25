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


    @GetMapping("/find-all")
    public ResponseEntity<List<TransactionResponse>> findAll(){
        var transactions = transactionRepository.findAll();
        return ResponseEntity.ok().body(TransactionResponse.convert(transactions));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> deposit(
            @RequestParam int currentAccountId,
               BigDecimal depositAmmount,
            UriComponentsBuilder uriComponentsBuilder){
        Optional<Account> account = accountRepository.findById(currentAccountId);
        Account currentAccount = account.get();
        currentAccount.setMoney(currentAccount.getMoney().add(depositAmmount));
        accountRepository.save(currentAccount);
        Transaction thisTransaction = new Transaction(0, currentAccount, currentAccountId, depositAmmount, TransactionType.DEPOSIT);
        transactionRepository.save(thisTransaction);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction));
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestParam int currentAccountId,
            BigDecimal withdrawAmmount,
            UriComponentsBuilder uriComponentsBuilder){

        Optional<Account> account = accountRepository.findById(currentAccountId);
        Account currentAccount = account.get();
        boolean hasEnoughMoney = currentAccount.getMoney().compareTo(withdrawAmmount) > 0;
        if(!hasEnoughMoney){
            throw new NotEnoughMoneyException();
        }
        currentAccount.setMoney(currentAccount.getMoney().subtract(withdrawAmmount));
        accountRepository.save(currentAccount);
        Transaction thisTransaction = new Transaction(0, currentAccount, currentAccountId, withdrawAmmount, TransactionType.WITHDRAW);
        transactionRepository.save(thisTransaction);
        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }

    @PostMapping
    public ResponseEntity<TransactionResponse> transferPix(
            @RequestParam int currentAccountId,
            @RequestParam int targetAccountId,
            BigDecimal transferAmmount,
            UriComponentsBuilder uriComponentsBuilder){

        Optional<Account> account = accountRepository.findById(currentAccountId);
        Account currentAccount = account.get();

        Optional<Account> targetAccounts = accountRepository.findById(currentAccountId);
        Account targetAccount = targetAccounts.get();

        boolean hasEnoughMoney = currentAccount.getMoney().compareTo(transferAmmount) > 0;
        if(!hasEnoughMoney){
            throw new NotEnoughMoneyException();
        }
        currentAccount.setMoney(currentAccount.getMoney().subtract(transferAmmount));
        accountRepository.save(currentAccount);
        targetAccount.setMoney(targetAccount.getMoney().add(transferAmmount));
        accountRepository.save(targetAccount);

        Transaction thisTransaction = new Transaction(0, currentAccount, targetAccountId, transferAmmount, TransactionType.TRANSFER_PIX);
        transactionRepository.save(thisTransaction);

        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }

    @PostMapping
    public ResponseEntity<TransactionResponse> transferTed(
            @RequestParam int currentAccountId,
            @RequestParam int targetAccountId,
            BigDecimal transferAmmount,
            UriComponentsBuilder uriComponentsBuilder){

        Optional<Account> account = accountRepository.findById(currentAccountId);
        Account currentAccount = account.get();

        Optional<Account> targetAccounts = accountRepository.findById(currentAccountId);
        Account targetAccount = targetAccounts.get();

        boolean hasEnoughMoney = currentAccount.getMoney().compareTo(transferAmmount) > 0;
        boolean weekend =  today == 6 || today == 7;
        boolean invalidHour = hour>17;
        if(!hasEnoughMoney){
            throw new NotEnoughMoneyException();
        }else if(weekend || invalidHour){
            throw new InvalidTimeException();
        }
        currentAccount.setMoney(currentAccount.getMoney().subtract(transferAmmount));
        accountRepository.save(currentAccount);
        targetAccount.setMoney(targetAccount.getMoney().add(transferAmmount));
        accountRepository.save(targetAccount);

        Transaction thisTransaction = new Transaction(0, currentAccount, targetAccountId, transferAmmount, TransactionType.TRANSFER_TED);
        transactionRepository.save(thisTransaction);

        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }

    @PostMapping
    public ResponseEntity<TransactionResponse> transferDoc(
            @RequestParam int currentAccountId,
            @RequestParam int targetAccountId,
            BigDecimal transferAmmount,
            UriComponentsBuilder uriComponentsBuilder){

        Optional<Account> account = accountRepository.findById(currentAccountId);
        Account currentAccount = account.get();

        Optional<Account> targetAccounts = accountRepository.findById(currentAccountId);
        Account targetAccount = targetAccounts.get();

        boolean hasEnoughMoney = currentAccount.getMoney().compareTo(transferAmmount) > 0;
        boolean weekend =  today == 6 || today == 7;
        boolean invalidHour = hour>22;
        boolean overTheLimit = transferAmmount.compareTo(new BigDecimal(5000)) == 1;
        if(!hasEnoughMoney){
            throw new NotEnoughMoneyException();
        }else if(weekend || invalidHour){
            throw new InvalidTimeException();
        }else if(overTheLimit){
            throw new OverTheLimitException();
        }
        currentAccount.setMoney(currentAccount.getMoney().subtract(transferAmmount));
        accountRepository.save(currentAccount);
        targetAccount.setMoney(targetAccount.getMoney().add(transferAmmount));
        accountRepository.save(targetAccount);

        Transaction thisTransaction = new Transaction(0, currentAccount, targetAccountId, transferAmmount, TransactionType.TRANSFER_DOC);
        transactionRepository.save(thisTransaction);

        URI uri =  uriComponentsBuilder.path("/transactions/{id}").
                buildAndExpand(thisTransaction.getId()).toUri();
        return ResponseEntity.created(uri)
                .body(new TransactionResponse(thisTransaction)); }



}
