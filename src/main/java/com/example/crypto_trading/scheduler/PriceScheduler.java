package com.example.crypto_trading.scheduler;

import com.example.crypto_trading.model.BinanceTradeData;
import com.example.crypto_trading.model.HuobiTradeData;
import com.example.crypto_trading.service.impl.BinanceServiceImpl;
import com.example.crypto_trading.service.impl.HuobiServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PriceScheduler {

    @Value("${crypto.tickers.supported}")
    private List<String> cryptoSupportedList;

    @Autowired
    private BinanceServiceImpl binanceService;

    @Autowired
    private HuobiServiceImpl huobiService;

    @Scheduled(fixedRateString = "${scheduler.price-poll-interval-ms}")
    public void fetchTickersBestPrice() throws JsonProcessingException {
        Map<String, BigDecimal[]> prices = new HashMap<>(); // {symbol, [bid, ask]}
        List<BinanceTradeData> binanceTradeDataList = binanceService.fetchTickers();
        List<HuobiTradeData> huobiTradeDataList = huobiService.fetchTickers();

        // Only Save ETHUSDT and ETHUSDT into price list
        binanceTradeDataList.stream()
                .filter(b -> cryptoSupportedList.contains(b.getSymbol()))
                .forEach(b -> {
                    String symbol = b.getSymbol();
                    BigDecimal bid = b.getBidPrice();
                    BigDecimal ask = b.getAskPrice();
                    prices.put(symbol, new BigDecimal[]{bid, ask});
                });

        // Compare the best Bid and Ask with binance
        huobiTradeDataList.stream()
                .filter(b -> cryptoSupportedList.contains(b.getSymbol()))
                .forEach(b -> {
                    String symbol = b.getSymbol();
                    BigDecimal bid = b.getBid();
                    BigDecimal ask = b.getAsk();

                    prices.compute(symbol, (k, exist) -> {
                        if (exist == null) {
                            return new BigDecimal[]{bid, ask};
                        } else {
                            BigDecimal bestBid = exist[0].max(bid);
                            BigDecimal bestAsk = exist[0].max(ask);
                            return new BigDecimal[]{bestBid, bestAsk};
                        }
                    });

                });

        try {
            System.out.println(new ObjectMapper().writeValueAsString(prices));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
