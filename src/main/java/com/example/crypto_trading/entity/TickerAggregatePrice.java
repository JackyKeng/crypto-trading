package com.example.crypto_trading.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TICKER_AGG_PRICE")
public class TickerAggregatePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String symbol;
    private BigDecimal bestBid;
    private BigDecimal bestAsk;

    @JsonIgnore
    private LocalDateTime createdDate;

    public TickerAggregatePrice(String symbol, BigDecimal bestBid, BigDecimal bestAsk) {
        this.symbol = symbol;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.createdDate = LocalDateTime.now();
    }
}
