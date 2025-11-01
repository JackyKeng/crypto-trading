package com.example.crypto_trading.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HuobiTradeData implements Serializable {

    private static final long serialVersionUID = -7352221014737720047L;

    private String symbol;
    private BigDecimal bid;
    private BigDecimal bidSize;
    private BigDecimal ask;
    private BigDecimal askSize;

}
