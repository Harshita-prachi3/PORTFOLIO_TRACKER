package com.example.trading_stimulitaor.controller;

import com.example.trading_stimulitaor.model.User;
import com.example.trading_stimulitaor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // For React frontend
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Login a user
     * @param credentials The user credentials for login
     * @return Success message or error
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required");
            }

            User user = userService.findByUsername(username);
            if (user != null) {
                if (passwordEncoder.matches(password, user.getPassword())) {
                    // Authentication successful
                    return ResponseEntity.ok("Login successful");
                } else {
                    return ResponseEntity.status(401).body("Invalid username or password");
                }
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Register a new user
     * @param user The user details for registration
     * @return The created user or error message
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Hash the password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User newUser = userService.createUser(user);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get user by username
     * @param username The username to look up
     * @return The user details or error message
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get all users in the system
     * @return List of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Update user profile
     * @param username The username of the user to update
     * @param updatedUser The updated user details
     * @return The updated user or error message
     */
    @PutMapping("/{username}/profile")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable String username,
            @RequestBody User updatedUser) {
        try {
            // Hash the password if it's being updated
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            User user = userService.updateUserProfile(username, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update account balance
     * @param username The username
     * @param balanceUpdate Map containing the new balance amount
     * @return Updated user or error message
     */
    @PutMapping("/{username}/balance")
    public ResponseEntity<?> updateBalance(
            @PathVariable String username,
            @RequestBody Map<String, Double> balanceUpdate) {
        try {
            Double newBalance = balanceUpdate.get("amount");
            if (newBalance == null) {
                return ResponseEntity.badRequest().body("Amount is required");
            }
            User user = userService.updateAccountBalance(username, newBalance);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Add to account balance
     * @param username The username
     * @param amount Map containing the amount to add
     * @return Updated user or error message
     */
    @PostMapping("/{username}/balance/add")
    public ResponseEntity<?> addToBalance(
            @PathVariable String username,
            @RequestBody Map<String, Double> amount) {
        try {
            Double addAmount = amount.get("amount");
            if (addAmount == null) {
                return ResponseEntity.badRequest().body("Amount is required");
            }
            User user = userService.addToAccountBalance(username, addAmount);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Subtract from account balance
     * @param username The username
     * @param amount Map containing the amount to subtract
     * @return Updated user or error message
     */
    @PostMapping("/{username}/balance/subtract")
    public ResponseEntity<?> subtractFromBalance(
            @PathVariable String username,
            @RequestBody Map<String, Double> amount) {
        try {
            Double subtractAmount = amount.get("amount");
            if (subtractAmount == null) {
                return ResponseEntity.badRequest().body("Amount is required");
            }
            User user = userService.subtractFromAccountBalance(username, subtractAmount);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Delete a user
     * @param username The username of the user to delete
     * @return Success message or error
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}