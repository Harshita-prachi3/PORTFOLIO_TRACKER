package com.example.trading_stimulitaor.service;

import com.example.trading_stimulitaor.model.Portfolio;
import com.example.trading_stimulitaor.model.User;
import com.example.trading_stimulitaor.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Portfolio updatePortfolio(String username, String symbol,
                                     Integer quantity, Double price,
                                     String tradeType) {
        // Validate user
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Fetch or create portfolio entry
        Portfolio portfolio = portfolioRepository.findByUserUsernameAndStockSymbol(username, symbol);
        if (portfolio == null) {
            portfolio = new Portfolio();
            portfolio.setUser(user);
            portfolio.setStockSymbol(symbol);
            portfolio.setQuantity(0);
            portfolio.setAveragePrice(0.0);
            portfolio.setTotalInvestment(0.0);
        }

        // Update portfolio based on trade type
        int oldQuantity = portfolio.getQuantity();
        double oldTotalInvestment = oldQuantity * portfolio.getAveragePrice();

        if (tradeType.equalsIgnoreCase("BUY")) {
            // Buying stocks: increase quantity, update average price
            int newQuantity = oldQuantity + quantity;
            double newTotalInvestment = oldTotalInvestment + (quantity * price);
            portfolio.setQuantity(newQuantity);
            portfolio.setAveragePrice(newTotalInvestment / newQuantity);
            portfolio.setTotalInvestment(newTotalInvestment);
        } else if (tradeType.equalsIgnoreCase("SELL")) {
            // Selling stocks: decrease quantity, adjust total investment
            if (oldQuantity < quantity) {
                throw new RuntimeException("Insufficient shares to sell");
            }
            int newQuantity = oldQuantity - quantity;
            portfolio.setQuantity(newQuantity);
            if (newQuantity > 0) {
                portfolio.setTotalInvestment(newQuantity * portfolio.getAveragePrice());
            } else {
                portfolio.setTotalInvestment(0.0);
                portfolio.setAveragePrice(0.0);
            }
        } else {
            throw new RuntimeException("Invalid trade type. Use 'BUY' or 'SELL'.");
        }

        // Update current value based on the latest price
        portfolio.setCurrentValue(portfolio.getQuantity() * price);

        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> getUserPortfolio(String username) {
        // Validate user
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Fetch all portfolio entries for the user
        return portfolioRepository.findByUserUsername(username);
    }
}