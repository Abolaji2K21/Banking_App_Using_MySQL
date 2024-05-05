package com.example.demo.service;

import com.example.demo.enums.TransactionType;
import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public Transaction recordTransaction(BigDecimal amount, String accountNumber, TransactionType transactionType){
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setDescription(transactionType.name());
       transaction = transactionRepository.save(transaction);

       return transaction;
    }

    public void recordCharge(BigDecimal amount, String accountNumber){
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setDescription("STAMP DUTY");
        transactionRepository.save(transaction);
    }

}
