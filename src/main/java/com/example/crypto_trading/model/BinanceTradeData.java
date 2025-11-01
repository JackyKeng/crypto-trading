package com.example.crypto_trading.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BinanceTradeData implements Serializable {

    private static final long serialVersionUID = -961314107009572426L;

    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private BigDecimal askPrice;
    private BigDecimal askQty;

}
