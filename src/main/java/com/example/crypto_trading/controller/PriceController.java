package com.example.crypto_trading.controller;

import com.example.crypto_trading.entity.TickerAggregatePrice;
import com.example.crypto_trading.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping("/latest")
    public List<TickerAggregatePrice> getLatestAggregatePrices() {
        return priceService.findLatestPrices();
    }
}
