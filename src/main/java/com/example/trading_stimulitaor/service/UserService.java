package com.example.trading_stimulitaor.service;

import com.example.trading_stimulitaor.model.User;
import com.example.trading_stimulitaor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Create a new user
     * @param user The user object to create
     * @return The created user
     * @throws RuntimeException if username or email already exists
     */
    public User createUser(User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set initial account balance if not set
        if (user.getAccountBalance() == null) {
            user.setAccountBalance(0.0);
        }

        return userRepository.save(user);
    }

    /**
     * Find user by their username
     * @param username The username to search for
     * @return The found user or null
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by their email
     * @param email The email to search for
     * @return The found user or null
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get user by their ID
     * @param id The user ID
     * @return Optional containing user if found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get all users in the system
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update user's account balance
     * @param username The username of the user
     * @param amount The new balance amount
     * @return Updated user object
     * @throws RuntimeException if user not found
     */
    public User updateAccountBalance(String username, Double amount) {
        User user = findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setAccountBalance(amount);
        return userRepository.save(user);
    }

    /**
     * Update user's profile information
     * @param username The username of the user to update
     * @param updatedUser The user object containing updated information
     * @return Updated user object
     * @throws RuntimeException if user not found
     */
    public User updateUserProfile(String username, User updatedUser) {
        User existingUser = findByUsername(username);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // Update fields if provided in updatedUser
        if (updatedUser.getEmail() != null) {
            // Check if new email is already used by another user
            User userWithEmail = findByEmail(updatedUser.getEmail());
            if (userWithEmail != null && !userWithEmail.getId().equals(existingUser.getId())) {
                throw new RuntimeException("Email already in use");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getFullName() != null) {
            existingUser.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    /**
     * Delete a user from the system
     * @param username The username of the user to delete
     * @throws RuntimeException if user not found
     */
    public void deleteUser(String username) {
        User user = findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        userRepository.delete(user);
    }

    /**
     * Verify user's password
     * @param rawPassword The raw password to check
     * @param encodedPassword The encoded password to check against
     * @return true if passwords match
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Add amount to user's account balance
     * @param username The username of the user
     * @param amount The amount to add
     * @return Updated user object
     * @throws RuntimeException if user not found
     */
    public User addToAccountBalance(String username, Double amount) {
        User user = findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setAccountBalance(user.getAccountBalance() + amount);
        return userRepository.save(user);
    }

    /**
     * Subtract amount from user's account balance
     * @param username The username of the user
     * @param amount The amount to subtract
     * @return Updated user object
     * @throws RuntimeException if user not found or insufficient balance
     */
    public User subtractFromAccountBalance(String username, Double amount) {
        User user = findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (user.getAccountBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        user.setAccountBalance(user.getAccountBalance() - amount);
        return userRepository.save(user);
    }
}