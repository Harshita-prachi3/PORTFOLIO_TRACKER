package com.example.trading_stimulitaor.controller;

import com.example.trading_stimulitaor.model.Stock;
import com.example.trading_stimulitaor.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    /**
     * Get real-time stock data
     */
    @GetMapping("/{symbol}/realtime")
    public ResponseEntity<?> getRealTimeStockData(@PathVariable String symbol) {
        try {
            logger.info("Fetching real-time data for symbol: {}", symbol);
            Stock stock = stockService.getStockData(symbol);
            return ResponseEntity.ok(stock);
        } catch (Exception e) {
            logger.error("Error fetching stock data for symbol {}: {}", symbol, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get stock by symbol from database
     */
    @GetMapping("/{symbol}")
    public ResponseEntity<?> getStock(@PathVariable String symbol) {
        Stock stock = stockService.getStock(symbol);
        if (stock != null) {
            logger.info("Fetching stock data from database for symbol: {}", symbol);
            return ResponseEntity.ok(stock);
        }
        logger.warn("Stock not found for symbol: {}", symbol);
        return ResponseEntity.notFound().build();
    }

    /**
     * Get all stocks
     */
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        logger.info("Fetching all stock data");
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }
}