package com.example.demo.model;

import com.example.demo.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pin;

    private BigDecimal amount;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private Long userId;

    @Column(unique = true)
    private String accountNumber;

    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private boolean changedPin;
}
