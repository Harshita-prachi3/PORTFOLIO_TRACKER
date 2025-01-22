package com.example.trading_stimulitaor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String stockSymbol;
    private Integer quantity;
    private Double averagePrice;
    private Double totalInvestment;
    private Double currentValue;

    @Column(name = "last_updated")
    private java.time.LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = java.time.LocalDateTime.now();
        totalInvestment = quantity * averagePrice;
    }
}