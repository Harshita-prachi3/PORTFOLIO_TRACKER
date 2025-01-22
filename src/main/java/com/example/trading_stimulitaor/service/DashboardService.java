package com.example.trading_stimulitaor.service;

import com.example.trading_stimulitaor.model.DashboardData;
import com.example.trading_stimulitaor.model.Portfolio;
import com.example.trading_stimulitaor.model.Trade;
import com.example.trading_stimulitaor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Add these imports
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class DashboardService {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserService userService;

    public DashboardData getDashboardData(String username) {
        // Validate user
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        try {
            // Get portfolio data
            List<Portfolio> portfolioList = portfolioService.getUserPortfolio(username);

            // Get recent trades
            List<Trade> recentTrades = tradeService.getUserTrades(username);

            // Create and return dashboard data
            return new DashboardData(
                    user.getAccountBalance(),
                    portfolioList,
                    recentTrades
            );

        } catch (Exception e) {
            throw new RuntimeException("Error fetching dashboard data: " + e.getMessage());
        }
    }

    public void updateDashboardData(String username) {
        // Add any real-time update logic here if needed
        // This could be used for WebSocket updates in the future
    }
}