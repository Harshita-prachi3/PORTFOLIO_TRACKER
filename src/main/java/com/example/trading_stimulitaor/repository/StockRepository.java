package com.example.trading_stimulitaor.repository;

import com.example.trading_stimulitaor.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Find stock by symbol (like AAPL, GOOGL)
    Stock findBySymbol(String symbol);

    // Check if stock exists by symbol
    boolean existsBySymbol(String symbol);
}