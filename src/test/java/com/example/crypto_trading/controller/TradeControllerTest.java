package com.example.crypto_trading.controller;

import com.example.crypto_trading.dto.TradeRequest;
import com.example.crypto_trading.enums.TradeActionEnum;
import com.example.crypto_trading.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TradeService tradeService;

    @Test
    void testPerformTradesApiCall() throws Exception {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setSymbol("TEST");
        tradeRequest.setTradeAction(TradeActionEnum.BUY);
        tradeRequest.setQuantity(new BigDecimal("0.1"));

        this.mockMvc.perform(post("/api/trades/performTrades")
                        .content(new ObjectMapper().writeValueAsString(tradeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));

        this.mockMvc.perform(post("/api/trades/performTrades")
                        .content(new ObjectMapper().writeValueAsString(tradeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-user-id", "123321"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
    }

    @Test
    void testFetchWalletsApiCall() throws Exception {
        when(tradeService.fetchWalletBalance(any())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(get("/api/trades/wallets"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));

        this.mockMvc.perform(get("/api/trades/wallets")
                        .header("x-user-id", "123321"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void testFetchHistoryApiCall() throws Exception {
        when(tradeService.fetchTradeTransactions(any())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(get("/api/trades/history"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));

        this.mockMvc.perform(get("/api/trades/history")
                        .header("x-user-id", "123321"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }
}
