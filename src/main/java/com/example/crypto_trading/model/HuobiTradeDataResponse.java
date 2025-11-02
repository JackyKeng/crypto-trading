package com.example.crypto_trading.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class HuobiTradeDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8435787402545985772L;

    List<HuobiTradeData> data;

}
