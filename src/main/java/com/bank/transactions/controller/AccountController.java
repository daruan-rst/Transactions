package com.bank.transactions.controller;


import com.bank.transactions.domain.Account;
import com.bank.transactions.repository.AccountRepository;
import com.bank.transactions.request.AccountRequest;
import com.bank.transactions.response.AccountResponse;
import com.bank.transactions.response.TransactionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    @PostMapping("/create-new")
    public ResponseEntity<AccountResponse> createNewAccount(
            @RequestBody AccountRequest accountRequest,
            UriComponentsBuilder uriComponentsBuilder
    ){
        Account account = accountRequest.convert();
        accountRepository.save(account);
        URI uri = uriComponentsBuilder.path("/accounts/{id}")
                .buildAndExpand(account.getAccountId()).toUri();
        return ResponseEntity.created(uri).body(new AccountResponse(account));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<AccountResponse>> findAll(){
        var account = accountRepository.findAll();
        return ResponseEntity.ok().body(AccountResponse.convert(account));}


    @DeleteMapping("/delete")
    public ResponseEntity<AccountResponse> deleteAccount(@RequestParam int accountId){
        accountRepository.deleteById(accountId);
        return ResponseEntity.ok().build();
    }

}
