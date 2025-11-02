package com.example.crypto_trading.service;

import com.example.crypto_trading.entity.TickerAggregatePrice;
import com.example.crypto_trading.repository.TickerAggregatePriceRepository;
import com.example.crypto_trading.service.impl.PriceServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class PriceServiceTest {

    @Autowired
    private TickerAggregatePriceRepository tickerAggregatePriceRepository;

    @Autowired
    private PriceServiceImpl priceService;

    @Test
    void testFindLatestPrices() {
        // Insert sample data into H2
        tickerAggregatePriceRepository.save(new TickerAggregatePrice(
                "BTCUSDT", new BigDecimal("0.1"), new BigDecimal("0.1"), LocalDateTime.now().minusMinutes(5)
        ));
        tickerAggregatePriceRepository.save(new TickerAggregatePrice(
                "BTCUSDT", new BigDecimal("0.2"), new BigDecimal("0.2"), LocalDateTime.now()
        ));
        tickerAggregatePriceRepository.save(new TickerAggregatePrice(
                "ETHUSDT", new BigDecimal("0.3"), new BigDecimal("0.3"), LocalDateTime.now()
        ));

        List<TickerAggregatePrice> latestPrices = priceService.findLatestPrices();

        assertThat(latestPrices).hasSize(2);
        assertThat(latestPrices)
                .extracting(TickerAggregatePrice::getSymbol)
                .containsExactlyInAnyOrder("BTCUSDT", "ETHUSDT");

        TickerAggregatePrice btc = latestPrices.stream()
                .filter(p -> p.getSymbol().equals("BTCUSDT")).findFirst().orElseThrow();
        assertThat(btc.getBestBid()).isEqualByComparingTo("0.2");
        assertThat(btc.getBestAsk()).isEqualByComparingTo("0.2");
    }

    @Test
    void testFindLatestPricesWithSymbol() {
        // Insert sample data into H2
        tickerAggregatePriceRepository.save(new TickerAggregatePrice(
                "BTCUSDT", new BigDecimal("0.1"), new BigDecimal("0.1"), LocalDateTime.now().minusMinutes(5)
        ));
        tickerAggregatePriceRepository.save(new TickerAggregatePrice(
                "BTCUSDT", new BigDecimal("0.2"), new BigDecimal("0.2"), LocalDateTime.now()
        ));

        TickerAggregatePrice latestPrice = priceService.findLatestPrices("BTCUSDT");
        assertThat(latestPrice).isNotNull();
        AssertionsForClassTypes.assertThat(latestPrice.getBestBid()).isEqualByComparingTo("0.2");
        AssertionsForClassTypes.assertThat(latestPrice.getBestAsk()).isEqualByComparingTo("0.2");
    }
}
