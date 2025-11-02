package com.example.crypto_trading.service;

import java.util.List;

public interface CryptoPriceService<T> {
    List<T> fetchTickers();
}
