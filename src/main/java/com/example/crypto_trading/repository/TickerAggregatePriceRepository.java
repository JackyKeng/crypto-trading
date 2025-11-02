package com.example.crypto_trading.repository;

import com.example.crypto_trading.entity.TickerAggregatePrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TickerAggregatePriceRepository extends JpaRepository<TickerAggregatePrice, Long> {

    Optional<TickerAggregatePrice> findTopBySymbolOrderByCreatedDateDesc(String symbol);

}
