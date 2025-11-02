package com.example.crypto_trading.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "WALLET_BALANCE")
public class WalletBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long userId;

    private String asset;
    private BigDecimal balance;

    public WalletBalance(Long userId, String asset, BigDecimal balance) {
        this.userId = userId;
        this.asset = asset;
        this.balance = balance;
    }
}
