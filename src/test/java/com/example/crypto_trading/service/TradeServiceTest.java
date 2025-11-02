package com.example.crypto_trading.service;

import com.example.crypto_trading.dto.TradeRequest;
import com.example.crypto_trading.entity.TickerAggregatePrice;
import com.example.crypto_trading.entity.TradeTransaction;
import com.example.crypto_trading.entity.WalletBalance;
import com.example.crypto_trading.enums.TradeActionEnum;
import com.example.crypto_trading.enums.WalletAssetTypeEnum;
import com.example.crypto_trading.repository.TickerAggregatePriceRepository;
import com.example.crypto_trading.repository.TradeTransactionRepository;
import com.example.crypto_trading.repository.WalletBalanceRepository;
import com.example.crypto_trading.service.impl.TradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.example.crypto_trading.enums.WalletAssetTypeEnum.BTCUSDT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TradeServiceTest {

    @Autowired
    private TradeServiceImpl tradeService;

    @Autowired
    private TickerAggregatePriceRepository tickerAggregatePriceRepository;

    @Autowired
    private WalletBalanceRepository walletBalanceRepository;

    @Autowired
    private TradeTransactionRepository tradeTransactionRepository;

    @BeforeEach
    void clear() {
        tickerAggregatePriceRepository.deleteAll();
        walletBalanceRepository.deleteAll();
        tradeTransactionRepository.deleteAll();
    }

    @Test
    void testPerformTrades_UnsupportedSymbol() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setSymbol("MOCK");
            tradeService.performTrades(1L, tradeRequest);
        });

        assertEquals("Unsupported Symbol", exception.getMessage());
    }

    @Test
    void testPerformTrades_NoPriceFound() {
        TickerAggregatePrice price = new TickerAggregatePrice(BTCUSDT.name(), new BigDecimal("0.1"), new BigDecimal("0.1"));
        tickerAggregatePriceRepository.save(price);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setSymbol(WalletAssetTypeEnum.ETHUSDT.name());
            tradeService.performTrades(1L, tradeRequest);
        });

        assertEquals("No price found for " + WalletAssetTypeEnum.ETHUSDT.name(), exception.getMessage());
    }

    @Test
    void testPerformTrades_MissingWallet() {
        TickerAggregatePrice price = new TickerAggregatePrice(BTCUSDT.name(), new BigDecimal("0.1"), new BigDecimal("0.1"));
        tickerAggregatePriceRepository.save(price);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setTradeAction(TradeActionEnum.BUY);
            tradeRequest.setSymbol(BTCUSDT.name());
            tradeRequest.setQuantity(new BigDecimal("0.15"));
            tradeService.performTrades(1L, tradeRequest);
        });

        assertEquals("Missing USDT wallet", exception.getMessage());

        WalletBalance walletBalance = new WalletBalance(1L, WalletAssetTypeEnum.USDT.name(), new BigDecimal("0.1"));
        walletBalanceRepository.save(walletBalance);

        exception = assertThrows(IllegalStateException.class, () -> {
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setTradeAction(TradeActionEnum.SELL);
            tradeRequest.setSymbol(BTCUSDT.name());
            tradeRequest.setQuantity(new BigDecimal("0.15"));
            tradeService.performTrades(1L, tradeRequest);
        });

        assertEquals("Missing " + BTCUSDT.name() + " wallet", exception.getMessage());
    }

    @Test
    void testPerformTrades_InsufficientUSDT() {
        TickerAggregatePrice price = new TickerAggregatePrice(BTCUSDT.name(), new BigDecimal("0.1"), new BigDecimal("0.1"));
        tickerAggregatePriceRepository.save(price);
        WalletBalance walletBalance = new WalletBalance(1L, WalletAssetTypeEnum.USDT.name(), new BigDecimal("10"));
        walletBalanceRepository.save(walletBalance);
        WalletBalance walletBalance1 = new WalletBalance(1L, WalletAssetTypeEnum.BTCUSDT.name(), new BigDecimal("0"));
        walletBalanceRepository.save(walletBalance1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setTradeAction(TradeActionEnum.BUY);
            tradeRequest.setSymbol(BTCUSDT.name());
            tradeRequest.setQuantity(new BigDecimal("1000"));
            tradeService.performTrades(1L, tradeRequest);
        });
        assertEquals("Insufficient USDT balance", exception.getMessage());
    }

    @Test
    void testPerformTrades_InsufficientBTCUSDT() {
        TickerAggregatePrice price = new TickerAggregatePrice(BTCUSDT.name(), new BigDecimal("0.1"), new BigDecimal("0.1"));
        tickerAggregatePriceRepository.save(price);
        WalletBalance walletBalance = new WalletBalance(1L, WalletAssetTypeEnum.USDT.name(), new BigDecimal("10"));
        walletBalanceRepository.save(walletBalance);
        WalletBalance walletBalance1 = new WalletBalance(1L, WalletAssetTypeEnum.BTCUSDT.name(), new BigDecimal("0"));
        walletBalanceRepository.save(walletBalance1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TradeRequest tradeRequest = new TradeRequest();
            tradeRequest.setTradeAction(TradeActionEnum.SELL);
            tradeRequest.setSymbol(BTCUSDT.name());
            tradeRequest.setQuantity(new BigDecimal("0.1"));
            tradeService.performTrades(1L, tradeRequest);
        });
        assertEquals("Insufficient " + BTCUSDT.name() + " balance", exception.getMessage());
    }

    @Test
    void testPerformTrades_Success() {
        TickerAggregatePrice price = new TickerAggregatePrice(BTCUSDT.name(), new BigDecimal("0.1"), new BigDecimal("0.1"));
        tickerAggregatePriceRepository.save(price);
        WalletBalance walletBalance = new WalletBalance(1L, WalletAssetTypeEnum.USDT.name(), new BigDecimal("10"));
        walletBalanceRepository.save(walletBalance);
        WalletBalance walletBalance1 = new WalletBalance(1L, WalletAssetTypeEnum.BTCUSDT.name(), new BigDecimal("0"));
        walletBalanceRepository.save(walletBalance1);

        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setTradeAction(TradeActionEnum.BUY);
        tradeRequest.setSymbol(BTCUSDT.name());
        tradeRequest.setQuantity(new BigDecimal("1"));
        tradeService.performTrades(1L, tradeRequest);

        List<TradeTransaction> transactions = tradeTransactionRepository.findByUserIdOrderByTransactionDateTimeDesc(1L).orElse(new ArrayList<>());
        assertEquals(1, transactions.size());

        tradeRequest = new TradeRequest();
        tradeRequest.setTradeAction(TradeActionEnum.SELL);
        tradeRequest.setSymbol(BTCUSDT.name());
        tradeRequest.setQuantity(new BigDecimal("1"));
        tradeService.performTrades(1L, tradeRequest);
        transactions = tradeTransactionRepository.findByUserIdOrderByTransactionDateTimeDesc(1L).orElse(new ArrayList<>());
        assertEquals(2, transactions.size());
    }

    @Test
    void testFetchWalletBalance() {
        WalletBalance walletBalance = new WalletBalance(1L, BTCUSDT.name(), new BigDecimal("0"));
        walletBalanceRepository.save(walletBalance);

        List<WalletBalance> result = tradeService.fetchWalletBalance(1L);
        assertEquals(1, result.size());

        result = tradeService.fetchWalletBalance(2L);
        assertEquals(0, result.size());
    }

    @Test
    void testFetchTradeTransactions() {
        TradeTransaction tradeTransaction = new TradeTransaction(1L, BTCUSDT.name(), TradeActionEnum.BUY, new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"));
        tradeTransactionRepository.save(tradeTransaction);

        List<TradeTransaction> result = tradeService.fetchTradeTransactions(1L);
        assertEquals(1, result.size());

        result = tradeService.fetchTradeTransactions(2L);
        assertEquals(0, result.size());
    }
}
