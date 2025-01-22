package com.example.trading_stimulitaor.repository;

import com.example.trading_stimulitaor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods
    User findByUsername(String username);
    boolean existsByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}