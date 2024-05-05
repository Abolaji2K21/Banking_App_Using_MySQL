package com.example.demo.dto;

import lombok.Data;

import java.security.SecureRandom;

@Data
public class UserSignUpResponse {

    private AccountCreationResponse accountCreationResponse;

    private String email;

    private String name;

}
