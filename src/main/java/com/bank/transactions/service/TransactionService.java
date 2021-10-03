package com.bank.transactions.service;

import com.bank.transactions.domain.Account;
import com.bank.transactions.domain.PhoneCreditValue;
import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionType;
import com.bank.transactions.exceptions.InvalidTimeException;
import com.bank.transactions.exceptions.NotEnoughMoneyException;
import com.bank.transactions.exceptions.OverTheLimitException;
import com.bank.transactions.repository.AccountRepository;
import com.bank.transactions.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Calendar now = Calendar.getInstance();
    private final int today = now.get(Calendar.DAY_OF_WEEK);
    private final int hour = now.get(Calendar.HOUR_OF_DAY);

    private boolean hasEnoughMoney(Account account, BigDecimal subtractMoney){
        return account.getMoney().compareTo(subtractMoney) > 0;
    }

    public Transaction deposit(int targetAccountId, BigDecimal depositAmmount){
        Account targetAccount = depositMoney(targetAccountId, depositAmmount);
        Transaction thisTransaction = new Transaction(0, targetAccount, targetAccountId, depositAmmount, TransactionType.DEPOSIT);
        transactionRepository.save(thisTransaction);
        return new Transaction(0, targetAccount, targetAccountId, depositAmmount, TransactionType.DEPOSIT);
    }

    private Account depositMoney(int targetAccountId, BigDecimal depositAmmount){
        Optional<Account> account = accountRepository.findById(targetAccountId);
        Account targetAccount = account.get();
        targetAccount.setMoney(targetAccount.getMoney().add(depositAmmount));
        accountRepository.save(targetAccount);
        return targetAccount;
    }

    public Transaction withdraw(int currentAccountId, BigDecimal withdrawAmmount){
        Account currentAccount = withdrawMoney(currentAccountId, withdrawAmmount);
        Transaction thisTransaction = new Transaction(0, currentAccount, currentAccountId, withdrawAmmount, TransactionType.PREPAID_CELL_CREDIT);
        transactionRepository.save(thisTransaction);
    return thisTransaction;
    }

    public Transaction phoneCredit(int currentAccountId, PhoneCreditValue credit){
        BigDecimal creditAmmount = new BigDecimal("0.00");
        switch (credit) {
            case CINCO:
                creditAmmount = new BigDecimal("5.00");
                break;
            case DEZ:
                creditAmmount = new BigDecimal("10.00");
                break;
            case VINTE:
                creditAmmount = new BigDecimal("20.00");
                break;
            case QUARENTA:
                creditAmmount = new BigDecimal("40.00");
                break;
        }

        Account currentAccount = withdrawMoney(currentAccountId, creditAmmount);
        Transaction thisTransaction = new Transaction(0, currentAccount, currentAccountId, creditAmmount, TransactionType.WITHDRAW);
        transactionRepository.save(thisTransaction);
        return thisTransaction;
    }

    public List<Transaction> bankStatement(int accountId){
        Optional<Account> accounts = accountRepository.findById(accountId);
        Account account = accounts.get();
        return transactionRepository.findTransactionsByCurrentAcc(account);
    }

    private Account withdrawMoney(int currentAccountId, BigDecimal withdrawAmmount){
        Optional<Account> account = accountRepository.findById(currentAccountId);
        Account currentAccount = account.get();
        if(!hasEnoughMoney(currentAccount, withdrawAmmount)){
            throw new NotEnoughMoneyException();
        }
        currentAccount.setMoney(currentAccount.getMoney().subtract(withdrawAmmount));
        accountRepository.save(currentAccount);
        return currentAccount;}

    public Transaction transfer(int currentAccountId, int targetAccountId, BigDecimal transferMoney, TransactionType transferType){
        Account currentAccount = withdrawMoney(currentAccountId, transferMoney);
        Account targetAccount = depositMoney(targetAccountId, transferMoney);
        Transaction thisTransaction = new Transaction(0, currentAccount, targetAccountId, transferMoney, transferType);
        transactionRepository.save(thisTransaction);
        return thisTransaction;
    }

    private boolean weekend(){
        return today == 6 || today == 7;
    }

    private boolean invalidHour(int limit){
        return hour>limit;
    }

    public void tedConditions(){
        if (weekend() || invalidHour(17)){
            throw new InvalidTimeException(); }
    }

    public void docConditions( BigDecimal transferAmmount ){
        boolean overTheLimit = transferAmmount.compareTo(new BigDecimal(5000)) == 1;
        if (weekend() || invalidHour(22)){
            throw new InvalidTimeException();
        }else if(overTheLimit){
            throw new OverTheLimitException();
        }
    }


}
