package com.example.crypto_trading.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "TICKER_AGG_PRICE")
public class TickerAggregatePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private BigDecimal bestBid;
    private BigDecimal bestAsk;
    private LocalDate createdDate;

    public TickerAggregatePrice(String symbol, BigDecimal bestBid, BigDecimal bestAsk) {
        this.symbol = symbol;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.createdDate = LocalDate.now();
    }
}
