package com.example.crypto_trading.service.impl;

import com.example.crypto_trading.entity.TickerAggregatePrice;
import com.example.crypto_trading.repository.TickerAggregatePriceRepository;
import com.example.crypto_trading.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    private TickerAggregatePriceRepository tickerAggregatePriceRepository;

    @Value("${crypto.tickers.supported}")
    private List<String> cryptoSupportedList;

    @Override
    public List<TickerAggregatePrice> findLatestPrices() {
        List<TickerAggregatePrice> result = new ArrayList<>();

        cryptoSupportedList.forEach(symbol -> {
            tickerAggregatePriceRepository.findTopBySymbolOrderByCreatedDateDesc(symbol).ifPresent(result::add);
        });
        return result;
    }
}
