package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class BankException extends Exception{
    private HttpStatus status;


    public BankException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
