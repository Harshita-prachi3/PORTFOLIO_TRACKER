package com.example.trading_stimulitaor.controller;

import com.example.trading_stimulitaor.model.Trade;
import com.example.trading_stimulitaor.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trades")
@CrossOrigin(origins = "http://localhost:3000")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * Execute a trade (buy/sell)
     */
    @PostMapping("/execute")
    public ResponseEntity<?> executeTrade(@RequestBody Map<String, Object> tradeRequest) {
        try {
            String username = (String) tradeRequest.get("username");
            String symbol = (String) tradeRequest.get("symbol");
            String tradeType = (String) tradeRequest.get("tradeType");
            Integer quantity = Integer.parseInt(tradeRequest.get("quantity").toString());
            Double price = Double.parseDouble(tradeRequest.get("price").toString());

            Trade trade = tradeService.executeTrade(username, symbol, tradeType, quantity, price);
            return ResponseEntity.ok(trade);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all trades for a specific user
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserTrades(@PathVariable String username) {
        try {
            List<Trade> trades = tradeService.getUserTrades(username);
            return ResponseEntity.ok(trades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all trades for a specific stock
     */
    @GetMapping("/stock/{symbol}")
    public ResponseEntity<?> getStockTrades(@PathVariable String symbol) {
        try {
            List<Trade> trades = tradeService.getStockTrades(symbol);
            return ResponseEntity.ok(trades);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}