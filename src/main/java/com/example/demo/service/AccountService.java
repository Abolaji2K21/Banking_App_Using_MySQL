package com.example.demo.service;

import com.example.demo.dto.AccountCreationResponse;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.BankException;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final TransactionService transactionService;

    private final BCryptPasswordEncoder passwordEncoder;

    public AccountCreationResponse createAccount(Long id, AccountType accountType, String pin) {
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setUserId(id);
        account.setAccountType(accountType);
        account.setPin(passwordEncoder.encode(pin));
        account = accountRepository.save(account);
        AccountCreationResponse accountCreationResponse = new AccountCreationResponse();
        accountCreationResponse.setAccountNumber(account.getAccountNumber());
        accountCreationResponse.setAccountType(accountType);
        accountCreationResponse.setAmount(account.getAmount());
        return accountCreationResponse;
    }

    @Transactional
    public void deposit(BigDecimal amount, String accountNumber) throws BankException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BankException("account number does not exist", HttpStatus.BAD_REQUEST);
        }
        account.setAmount(account.getAmount().add(amount));
        transactionService.recordTransaction(amount, accountNumber, TransactionType.DEPOSIT);
        if (amount.compareTo(new BigDecimal(10000)) > 0) {
            account.setAmount(account.getAmount().subtract(new BigDecimal(50)));
            transactionService.recordCharge(new BigDecimal(50), accountNumber);
        }
        accountRepository.save(account);

    }

    @Transactional
    public void withdraw(BigDecimal amount, String accountNumber, String pin) throws BankException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BankException("account number does not exist", HttpStatus.BAD_REQUEST);
        }
        String encodedPin = passwordEncoder.encode(pin);
        if (!encodedPin.matches(account.getPin())){
            throw new BankException("Invalid credentials", HttpStatus.BAD_REQUEST);
        }
        if (amount.compareTo(account.getAmount()) > 0){
            throw new BankException("insufficient balance", HttpStatus.BAD_REQUEST);

        }
        account.setAmount(account.getAmount().subtract(amount));
        transactionService.recordTransaction(amount, accountNumber, TransactionType.WITHDRAWAL);
        if (amount.compareTo(new BigDecimal(10000)) > 0) {
            account.setAmount(account.getAmount().subtract(new BigDecimal(50)));
            transactionService.recordCharge(new BigDecimal(50), accountNumber);
        }
        accountRepository.save(account);

    }

    public String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

}
