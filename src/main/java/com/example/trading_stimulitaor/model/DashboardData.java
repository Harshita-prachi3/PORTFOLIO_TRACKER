package com.example.trading_stimulitaor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardData {
    private Double accountBalance;
    private Double totalPortfolioValue;
    private List<Portfolio> portfolio;
    private List<Trade> recentTrades;
    private Integer totalTrades;
    private Double totalProfitLoss;

    // Custom constructor for easy initialization
    public DashboardData(Double accountBalance, List<Portfolio> portfolio, List<Trade> recentTrades) {
        this.accountBalance = accountBalance;
        this.portfolio = portfolio;
        this.recentTrades = recentTrades;
        this.totalTrades = recentTrades != null ? recentTrades.size() : 0;

        // Calculate total portfolio value
        this.totalPortfolioValue = portfolio != null ?
                portfolio.stream()
                        .mapToDouble(Portfolio::getCurrentValue)
                        .sum() : 0.0;

        // Calculate total profit/loss
        this.totalProfitLoss = portfolio != null ?
                portfolio.stream()
                        .mapToDouble(p -> (p.getCurrentValue() - (p.getAveragePrice() * p.getQuantity())))
                        .sum() : 0.0;
    }
}