package com.example.trading_stimulitaor.repository;

import com.example.trading_stimulitaor.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserUsername(String username);
    Portfolio findByUserUsernameAndStockSymbol(String username, String stockSymbol);
}