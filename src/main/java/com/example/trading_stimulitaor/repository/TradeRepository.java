package com.example.trading_stimulitaor.repository;

import com.example.trading_stimulitaor.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUserUsername(String username);
    List<Trade> findByStockSymbol(String symbol);
}