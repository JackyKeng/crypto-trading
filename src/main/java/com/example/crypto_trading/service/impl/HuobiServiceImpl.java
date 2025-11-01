package com.example.crypto_trading.service.impl;

import com.example.crypto_trading.model.HuobiTradeData;
import com.example.crypto_trading.model.HuobiTradeDataResponse;
import com.example.crypto_trading.service.CryptoPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class HuobiServiceImpl implements CryptoPriceService<HuobiTradeData> {

    @Value("${api.huobi.tickers.path}")
    private String huobiTickersApiPath;

    @Autowired
    private WebClient huobiWebClient;

    @Override
    public List<HuobiTradeData> fetchTickers() {

        Flux<HuobiTradeData> flux = huobiWebClient.get()
                .uri(huobiTickersApiPath)
                .retrieve()
                .bodyToMono(HuobiTradeDataResponse.class)       // fetch wrapper object
                .flatMapMany(res -> {                          // convert Mono -> Flux
                    if (res.getData() == null || res.getData().isEmpty()) {
                        return Flux.empty();
                    }
                    return Flux.fromIterable(res.getData());   // stream each HuobiTradeData
                });
        return flux.collectList().block();
    }
}
