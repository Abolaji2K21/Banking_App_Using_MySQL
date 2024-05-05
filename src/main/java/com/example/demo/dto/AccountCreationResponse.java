package com.example.demo.dto;

import com.example.demo.enums.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreationResponse {

    private String accountNumber;

    private BigDecimal amount;

    private AccountType accountType;
}
