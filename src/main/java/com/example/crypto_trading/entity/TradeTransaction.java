package com.example.crypto_trading.entity;

import com.example.crypto_trading.enums.TradeActionEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TRADE_TRANSACTION")
public class TradeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private Long userId;
    private String symbol;
    private String tradeAction;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal total;
    private LocalDateTime transactionDateTime;

    public TradeTransaction(Long userId, String symbol, TradeActionEnum tradeAction, BigDecimal price, BigDecimal quantity, BigDecimal total) {
        this.userId = userId;
        this.symbol = symbol;
        this.tradeAction = tradeAction.name();
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.transactionDateTime = LocalDateTime.now();
    }

}
