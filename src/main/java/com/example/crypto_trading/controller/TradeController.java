package com.example.crypto_trading.controller;

import com.example.crypto_trading.dto.TradeRequest;
import com.example.crypto_trading.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/placeTrade")
    public ResponseEntity<?> placeTrade(@RequestHeader(value = "x-user-id", required = false) Long userId,
                                        @Valid @RequestBody TradeRequest tradeRequest) {
        // Mock user id
        if (userId == null) userId = 1L;

        tradeService.performTrades(userId, tradeRequest);

        return ResponseEntity.ok("OK");
    }
}
