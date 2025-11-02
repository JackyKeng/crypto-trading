package com.example.crypto_trading.repository;


import com.example.crypto_trading.entity.WalletBalance;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Transactional
public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT wb FROM WalletBalance wb WHERE wb.userId = :userId AND wb.asset = :asset")
    Optional<WalletBalance> findByUserIdAndAssetForUpdate(@Param("userId") Long userId, @Param("asset") String asset);
}
