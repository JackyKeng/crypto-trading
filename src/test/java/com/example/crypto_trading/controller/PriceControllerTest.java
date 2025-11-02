package com.example.crypto_trading.controller;

import com.example.crypto_trading.service.PriceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceService priceService;

    @Test
    void testGetLatestAggregatePricesApiCall() throws Exception {
        Mockito.when(priceService.findLatestPrices()).thenReturn(new ArrayList<>());

        this.mockMvc.perform(get("/api/prices/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
