package com.example.trading_stimulitaor.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String stockSymbol;
    private String tradeType;  // BUY or SELL
    private Integer quantity;
    private Double price;
    private Double totalAmount;

    @Column(name = "trade_date")
    private LocalDateTime tradeDate;

    @PrePersist
    protected void onCreate() {
        tradeDate = LocalDateTime.now();
        totalAmount = price * quantity;
    }
}