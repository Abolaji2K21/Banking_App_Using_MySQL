package com.example.demo.service;

import com.example.demo.dto.AccountCreationResponse;
import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UserSignUpResponse;
import com.example.demo.exception.BankException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {

    private final UserRepository userRepository;

    private final AccountService accountService;

    @Transactional
    public UserSignUpResponse signUp(CreateUserRequest createUserRequest) throws BankException {
        User user = userRepository.findByEmail(createUserRequest.getEmail());
        if (user != null) {
            throw new BankException("user with email already exists", HttpStatus.BAD_REQUEST);
        }
        user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user = userRepository.save(user);

        AccountCreationResponse accountCreationResponse = accountService.createAccount(user.getId(), createUserRequest.getAccountType(), createUserRequest.getPin());
        UserSignUpResponse userSignUpResponse = new UserSignUpResponse();
        userSignUpResponse.setEmail(user.getEmail());
        userSignUpResponse.setName(user.getName());
        userSignUpResponse.setAccountCreationResponse(accountCreationResponse);
        return userSignUpResponse;
    }
}
