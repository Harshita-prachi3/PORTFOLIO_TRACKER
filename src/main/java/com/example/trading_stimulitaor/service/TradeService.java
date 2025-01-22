package com.example.trading_stimulitaor.service;

import com.example.trading_stimulitaor.model.*;
import com.example.trading_stimulitaor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StockService stockService;

    @Autowired
    private PortfolioService portfolioService;  // For updating the portfolio

    @Transactional
    public Trade executeTrade(String username, String symbol, String tradeType,
                              Integer quantity, Double price) {
        // Get user
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Calculate total amount
        Double totalAmount = price * quantity;

        // Handle trade type (BUY or SELL)
        if (tradeType.equalsIgnoreCase("BUY")) {
            if (user.getAccountBalance() < totalAmount) {
                throw new RuntimeException("Insufficient balance");
            }
            userService.subtractFromAccountBalance(username, totalAmount); // Deduct amount for BUY
        } else if (tradeType.equalsIgnoreCase("SELL")) {
            userService.addToAccountBalance(username, totalAmount); // Add amount for SELL
        } else {
            throw new RuntimeException("Invalid trade type. Use 'BUY' or 'SELL'.");
        }

        // Create and save trade
        Trade trade = new Trade();
        trade.setUser(user);
        trade.setStockSymbol(symbol);
        trade.setTradeType(tradeType);
        trade.setQuantity(quantity);
        trade.setPrice(price);

        trade = tradeRepository.save(trade);

        // Update portfolio after trade
        portfolioService.updatePortfolio(username, symbol, quantity, price, tradeType);

        return trade;
    }

    public List<Trade> getUserTrades(String username) {
        // Retrieve all trades for a specific user
        return tradeRepository.findByUserUsername(username);
    }

    public List<Trade> getStockTrades(String symbol) {
        // Retrieve all trades for a specific stock
        return tradeRepository.findByStockSymbol(symbol);
    }
}