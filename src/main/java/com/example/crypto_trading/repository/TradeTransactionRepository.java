package com.example.crypto_trading.repository;

import com.example.crypto_trading.entity.TradeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {

    Optional<List<TradeTransaction>> findByUserIdOrderByTransactionDateTimeDesc(Long userId);
}
