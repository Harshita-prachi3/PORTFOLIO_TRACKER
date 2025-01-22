package com.example.trading_stimulitaor.service;

import com.example.trading_stimulitaor.model.Stock;
import com.example.trading_stimulitaor.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StockRepository stockRepository;

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Value("${alphavantage.api.baseurl}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Stock getStockData(String symbol) {
        try {
            String url = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                    baseUrl, symbol, apiKey);
            logger.info("Calling Alpha Vantage API");

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);

            if (responseMap == null || !responseMap.containsKey("Global Quote") ||
                    ((Map)responseMap.get("Global Quote")).isEmpty()) {
                // If no data, try to return cached data
                Stock cachedStock = stockRepository.findBySymbol(symbol);
                if (cachedStock != null) {
                    logger.info("Returning cached data for symbol: {}", symbol);
                    return cachedStock;
                }
                throw new RuntimeException("No stock data available for symbol: " + symbol);
            }

            Map<String, String> quote = (Map<String, String>) responseMap.get("Global Quote");

            Stock stock = stockRepository.findBySymbol(symbol);
            if (stock == null) {
                stock = new Stock();
                stock.setSymbol(symbol);
            }

            // Update stock data with error handling for each field
            updateStockData(stock, quote);
            stock.setLastUpdated(LocalDateTime.now());

            return stockRepository.save(stock);

        } catch (HttpClientErrorException e) {
            logger.error("API call failed: {}", e.getMessage());
            throw new RuntimeException("API service unavailable. Please try again later.");
        } catch (Exception e) {
            logger.error("Error fetching stock data: {}", e.getMessage());

            // Try to return cached data if available
            Stock cachedStock = stockRepository.findBySymbol(symbol);
            if (cachedStock != null) {
                logger.info("Returning cached data for symbol: {}", symbol);
                return cachedStock;
            }

            throw new RuntimeException("Unable to fetch stock data. Please try again later.");
        }
    }

    private void updateStockData(Stock stock, Map<String, String> quote) {
        try {
            stock.setCurrentPrice(parseDouble(quote.get("05. price")));
            stock.setOpenPrice(parseDouble(quote.get("02. open")));
            stock.setHighPrice(parseDouble(quote.get("03. high")));
            stock.setLowPrice(parseDouble(quote.get("04. low")));
            stock.setVolume(parseLong(quote.get("06. volume")));
        } catch (Exception e) {
            logger.error("Error updating stock data: {}", e.getMessage());
            throw new RuntimeException("Error processing stock data");
        }
    }

    public Stock getStock(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    private Double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value.replace("$", "").trim()) : 0.0;
        } catch (NumberFormatException e) {
            logger.warn("Error parsing double value: {}", value);
            return 0.0;
        }
    }

    private Long parseLong(String value) {
        try {
            return value != null ? Long.parseLong(value.replace(",", "").trim()) : 0L;
        } catch (NumberFormatException e) {
            logger.warn("Error parsing long value: {}", value);
            return 0L;
        }
    }
}