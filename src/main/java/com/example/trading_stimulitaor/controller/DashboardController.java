package com.example.trading_stimulitaor.controller;

import com.example.trading_stimulitaor.model.DashboardData;
import com.example.trading_stimulitaor.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Add these imports
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getDashboardData(@PathVariable String username) {
        try {
            logger.info("Fetching dashboard data for user: {}", username);
            DashboardData dashboardData = dashboardService.getDashboardData(username);
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            logger.error("Error fetching dashboard data for user {}: {}", username, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{username}/summary")
    public ResponseEntity<?> getDashboardSummary(@PathVariable String username) {
        try {
            logger.info("Fetching dashboard summary for user: {}", username);
            DashboardData dashboardData = dashboardService.getDashboardData(username);
            Map<String, Object> summary = new HashMap<>();
            summary.put("accountBalance", dashboardData.getAccountBalance());
            summary.put("totalPortfolioValue", dashboardData.getTotalPortfolioValue());
            summary.put("totalProfitLoss", dashboardData.getTotalProfitLoss());
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            logger.error("Error fetching dashboard summary for user {}: {}", username, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}