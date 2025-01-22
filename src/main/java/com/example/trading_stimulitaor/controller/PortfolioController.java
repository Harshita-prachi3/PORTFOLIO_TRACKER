package com.example.trading_stimulitaor.controller;

import com.example.trading_stimulitaor.model.Portfolio;
import com.example.trading_stimulitaor.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:3000")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserPortfolio(@PathVariable String username) {
        try {
            List<Portfolio> portfolio = portfolioService.getUserPortfolio(username);
            return ResponseEntity.ok(portfolio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}