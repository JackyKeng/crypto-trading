package com.example.crypto_trading.scheduler;

import com.example.crypto_trading.entity.TickerAggregatePrice;
import com.example.crypto_trading.model.BinanceTradeData;
import com.example.crypto_trading.model.HuobiTradeData;
import com.example.crypto_trading.repository.TickerAggregatePriceRepository;
import com.example.crypto_trading.service.impl.BinanceServiceImpl;
import com.example.crypto_trading.service.impl.HuobiServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PriceSchedulerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TickerAggregatePriceRepository tickerAggregatePriceRepository;

    @Autowired
    private PriceScheduler priceScheduler;

    @MockitoBean
    private BinanceServiceImpl binanceService; // Spring replaces bean with mock

    @MockitoBean
    private HuobiServiceImpl huobiService;

    @Test
    void testPriceScheduler() throws Exception {
        String binanceTickersJson = readJson("json/MockBinanceTickers.json");
        String huobiTickersJson = readJson("json/MockHuobiTickers.json");

        List<BinanceTradeData> binanceList = objectMapper.readValue(
                binanceTickersJson,
                new TypeReference<List<BinanceTradeData>>() {
                }
        );
        List<HuobiTradeData> huobiList = objectMapper.readValue(
                huobiTickersJson,
                new TypeReference<List<HuobiTradeData>>() {
                }
        );
        when(binanceService.fetchTickers()).thenReturn(binanceList);
        when(huobiService.fetchTickers()).thenReturn(huobiList);

        priceScheduler.fetchTickersBestPrice();

        List<TickerAggregatePrice> savedPrices = tickerAggregatePriceRepository.findAll();
        assertThat(savedPrices).hasSize(2);
        assertThat(savedPrices).extracting(TickerAggregatePrice::getSymbol)
                .containsExactlyInAnyOrder("BTCUSDT", "ETHUSDT");
    }

    private String readJson(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        Object json = objectMapper.readValue(inputStream, Object.class);
        return objectMapper.writeValueAsString(json); // ensures it's valid JSON string
    }
}
