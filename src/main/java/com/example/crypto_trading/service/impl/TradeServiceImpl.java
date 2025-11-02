package com.example.crypto_trading.service.impl;

import com.example.crypto_trading.dto.TradeRequest;
import com.example.crypto_trading.entity.TickerAggregatePrice;
import com.example.crypto_trading.entity.WalletBalance;
import com.example.crypto_trading.enums.TradeActionEnum;
import com.example.crypto_trading.enums.WalletAssetTypeEnum;
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


    @Override
    public void performTrades(Long userId, TradeRequest request) {
        if (!cryptoSupportedList.contains(request.getSymbol())) {
            throw new IllegalArgumentException("Unsupported Symbol");
        }

        // Get latest price
        TickerAggregatePrice tickerAggregatePrice = priceService.findLatestPrices(request.getSymbol());
        if (ObjectUtils.isEmpty(tickerAggregatePrice))
            throw new IllegalStateException("No price found for " + request.getSymbol());

        // Check sufficient amount
        BigDecimal price = TradeActionEnum.BUY.equals(request.getTradeAction()) ? tickerAggregatePrice.getBestAsk() : tickerAggregatePrice.getBestBid();
        BigDecimal total = price.multiply(request.getQuantity());
        WalletBalance usdtWalletBalance = walletBalanceRepository.findByUserIdAndAssetForUpdate(userId, WalletAssetTypeEnum.USDT.name())
                .orElseThrow(() -> new IllegalStateException("Missing USDT wallet"));
        WalletBalance assetWalletBalance = walletBalanceRepository.findByUserIdAndAssetForUpdate(userId, request.getSymbol())
                .orElseThrow(() -> new IllegalStateException("Missing " + request.getSymbol() + " wallet"));

        if (TradeActionEnum.BUY.equals(request.getTradeAction())) {
            if (usdtWalletBalance.getBalance().compareTo(total) < 0)
                throw new IllegalArgumentException("Insufficient USDT balance");
            BigDecimal remainingBalance = usdtWalletBalance.getBalance().subtract(total);
            usdtWalletBalance.setBalance(remainingBalance);
            assetWalletBalance.setBalance(assetWalletBalance.getBalance().add(request.getQuantity()));
        } else {
            if (assetWalletBalance.getBalance().compareTo(request.getQuantity()) < 0)
                throw new IllegalArgumentException("Insufficient " + request.getSymbol() + " balance");
            assetWalletBalance.setBalance(assetWalletBalance.getBalance().subtract(request.getQuantity()));
            usdtWalletBalance.setBalance(usdtWalletBalance.getBalance().add(total));
        }

        // Insert into wallet balance
        walletBalanceRepository.save(usdtWalletBalance);
        walletBalanceRepository.save(assetWalletBalance);
    }

    @Override
    public List<WalletBalance> fetchWalletBalance(Long userId) {
        return walletBalanceRepository.findByUserId(userId).orElse(new ArrayList<>());
    }
}
