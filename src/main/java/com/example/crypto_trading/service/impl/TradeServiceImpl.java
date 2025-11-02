package com.example.crypto_trading.service.impl;

import com.example.crypto_trading.dto.TradeRequest;
import com.example.crypto_trading.entity.TickerAggregatePrice;
import com.example.crypto_trading.entity.TradeTransaction;
import com.example.crypto_trading.entity.WalletBalance;
import com.example.crypto_trading.enums.TradeActionEnum;
import com.example.crypto_trading.enums.WalletAssetTypeEnum;
import com.example.crypto_trading.repository.TradeTransactionRepository;
import com.example.crypto_trading.repository.WalletBalanceRepository;
import com.example.crypto_trading.service.PriceService;
import com.example.crypto_trading.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    @Value("${crypto.tickers.supported}")
    private List<String> cryptoSupportedList;

    @Autowired
    private PriceService priceService;

    @Autowired
    private WalletBalanceRepository walletBalanceRepository;

    @Autowired
    private TradeTransactionRepository tradeTransactionRepository;


    @Override
    public void performTrades(Long userId, TradeRequest request) {
        String symbol = request.getSymbol();
        BigDecimal quantity = request.getQuantity();
        TradeActionEnum tradeAction = request.getTradeAction();

        if (!cryptoSupportedList.contains(request.getSymbol())) {
            throw new IllegalArgumentException("Unsupported Symbol");
        }


        // Get latest price
        TickerAggregatePrice tickerAggregatePrice = priceService.findLatestPrices(symbol);
        if (ObjectUtils.isEmpty(tickerAggregatePrice))
            throw new IllegalStateException("No price found for " + symbol);

        // Check sufficient amount
        BigDecimal price = TradeActionEnum.BUY.equals(tradeAction) ? tickerAggregatePrice.getBestAsk() : tickerAggregatePrice.getBestBid();
        BigDecimal total = price.multiply(quantity);
        WalletBalance usdtWalletBalance = walletBalanceRepository.findByUserIdAndAssetForUpdate(userId, WalletAssetTypeEnum.USDT.name())
                .orElseThrow(() -> new IllegalStateException("Missing USDT wallet"));
        WalletBalance assetWalletBalance = walletBalanceRepository.findByUserIdAndAssetForUpdate(userId, symbol)
                .orElseThrow(() -> new IllegalStateException("Missing " + symbol + " wallet"));

        if (TradeActionEnum.BUY.equals(tradeAction)) {
            if (usdtWalletBalance.getBalance().compareTo(total) < 0)
                throw new IllegalArgumentException("Insufficient USDT balance");
            BigDecimal remainingBalance = usdtWalletBalance.getBalance().subtract(total);
            usdtWalletBalance.setBalance(remainingBalance);
            assetWalletBalance.setBalance(assetWalletBalance.getBalance().add(quantity));
        } else {
            if (assetWalletBalance.getBalance().compareTo(quantity) < 0)
                throw new IllegalArgumentException("Insufficient " + symbol + " balance");
            assetWalletBalance.setBalance(assetWalletBalance.getBalance().subtract(quantity));
            usdtWalletBalance.setBalance(usdtWalletBalance.getBalance().add(total));
        }

        // Insert into wallet balance
        walletBalanceRepository.save(usdtWalletBalance);
        walletBalanceRepository.save(assetWalletBalance);

        TradeTransaction tradeTransaction = new TradeTransaction(userId, symbol, tradeAction, price, quantity, total);
        tradeTransactionRepository.save(tradeTransaction);
    }

    @Override
    public List<WalletBalance> fetchWalletBalance(Long userId) {
        return walletBalanceRepository.findByUserId(userId).orElse(new ArrayList<>());
    }

    @Override
    public List<TradeTransaction> fetchTradeTransactions(Long userId) {
        return tradeTransactionRepository.findByUserIdOrderByTransactionDateTimeDesc(userId).orElse(new ArrayList<>());
    }
}
