package com.example.crypto_trading.service;

import com.example.crypto_trading.dto.TradeRequest;
import com.example.crypto_trading.entity.TradeTransaction;
import com.example.crypto_trading.entity.WalletBalance;

import java.util.List;

public interface TradeService {

    void performTrades(Long userId, TradeRequest request);

    List<WalletBalance> fetchWalletBalance(Long userId);

    List<TradeTransaction> fetchTradeTransactions(Long userId);

}
