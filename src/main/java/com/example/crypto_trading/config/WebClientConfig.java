package com.example.crypto_trading.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${api.binance.base-url}")
    private String binanceApiBaseUrl;

    @Value("${api.huobi.base-url}")
    private String huobiApiBaseUrl;

    @Bean
    public WebClient binanceWebClient() {
        return WebClient.builder()
                .baseUrl(binanceApiBaseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024) // 16 MB
                        ).build())
                .build();
    }

    @Bean
    public WebClient huobiWebClient() {
        return WebClient.builder()
                .baseUrl(huobiApiBaseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024) // 16 MB
                        ).build())
                .build();
    }
}
