package com.example.crypto_trading.dto;

import com.example.crypto_trading.enums.TradeActionEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TradeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7120515545680339609L;

    @NotBlank
    private String symbol;

    @NotNull
    private TradeActionEnum tradeAction;

    @NotNull
    @DecimalMin(value = "0.0000001")
    private BigDecimal quantity;

}
