package com.example.demo.dto;

import com.example.demo.enums.AccountType;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;

    private String name;

    private String password;

    private AccountType accountType;


    private String pin;
}
