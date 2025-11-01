package com.example.crypto_trading.service.impl;

import com.example.crypto_trading.model.BinanceTradeData;
import com.example.crypto_trading.service.CryptoPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class BinanceServiceImpl implements CryptoPriceService<BinanceTradeData> {

    @Value("${api.binance.tickers.path}")
    private String binanceTickersApiPath;

    @Autowired
    private WebClient binanceWebClient;

    @Override
    public List<BinanceTradeData> fetchTickers() {
        Flux<BinanceTradeData> flux = binanceWebClient.get()
                .uri(binanceTickersApiPath)
                .retrieve()
                .bodyToMono(BinanceTradeData[].class)
                .flatMapMany(array -> array != null ? Flux.fromArray(array) : Flux.empty());

        return flux.collectList().block();
    }
}
