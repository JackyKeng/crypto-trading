package com.example.crypto_trading.service;

import com.example.crypto_trading.entity.TickerAggregatePrice;

import java.util.List;

public interface PriceService {
    List<TickerAggregatePrice> findLatestPrices();

    TickerAggregatePrice findLatestPrices(String symbol);
}
