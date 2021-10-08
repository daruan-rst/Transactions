package com.bank.transactions.controller;


import com.bank.transactions.client.UserClient;
import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.AccountType;
import com.bank.transactions.exceptions.InvalidUserException;
import com.bank.transactions.repository.AccountRepository;
import com.bank.transactions.request.AccountRequest;
import com.bank.transactions.response.AccountResponse;
import com.bank.transactions.response.TransactionResponse;
import com.bank.transactions.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @GetMapping("/find-by-accoun-id/{id}")
    public Account findById(@RequestParam int id){
        return accountRepository.findAccountByAccountId(id);
    }

    @PostMapping("/create-new")
    public ResponseEntity<AccountResponse> createNewAccount(
            @RequestParam AccountType accountType,
            @RequestBody AccountRequest accountRequest,
            UriComponentsBuilder uriComponentsBuilder
    ){
        Account account = accountService.userVerification(accountRequest, accountType);
        accountRepository.save(account);
        URI uri = uriComponentsBuilder.path("/accounts/{id}")
                .buildAndExpand(account.getAccountId()).toUri();
        return ResponseEntity.created(uri).body(new AccountResponse(account));
    }


    @GetMapping("/find-all")
    public ResponseEntity<List<AccountResponse>> findAll(){
        var account = accountRepository.findAll();
        return ResponseEntity.ok().body(AccountResponse.convert(account));}

    @GetMapping("/balance")
    public ResponseEntity<String> balance(@RequestParam int accountId){
        return ResponseEntity.ok().body(accountService.balance(accountId));
    }

    @PutMapping("/update-money/{accountId}")
    public void updateMoney(@RequestParam int accountId, @RequestParam BigDecimal money){
        Account thisAccount = accountRepository.findAccountByAccountId(accountId);
        thisAccount.setMoney(thisAccount.getMoney().add(money));
        accountRepository.save(thisAccount);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<AccountResponse> deleteAccount(@RequestParam int accountId){
        accountRepository.deleteById(accountId);
        return ResponseEntity.ok().build();
    }

}
