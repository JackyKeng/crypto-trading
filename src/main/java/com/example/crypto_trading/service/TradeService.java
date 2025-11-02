package com.example.crypto_trading.service;

import com.example.crypto_trading.dto.TradeRequest;

public interface TradeService {

    void performTrades(Long userId, TradeRequest request);

}
