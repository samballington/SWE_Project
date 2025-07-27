package com.example.obs.service;

import com.example.obs.model.User;
import com.example.obs.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Transactional
    public User registerNewUser(User user) {
        logger.info("Registering new user: {}", user.getUsername());
        
        // Validate the user doesn't already exist
        if (usernameExists(user.getUsername())) {
            logger.error("Username already exists: {}", user.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (emailExists(user.getEmail())) {
            logger.error("Email already exists: {}", user.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        
        // Set user as disabled until email verification
        user.setEnabled(false);
        user.setEmailVerified(false);
        
        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours
        
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getId());
        
        // Send verification email
        try {
            emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), verificationToken);
            logger.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send verification email", e);
            // Don't throw exception here, user is still registered
        }
        
        return savedUser;
    }
    
    @Transactional
    public boolean verifyEmail(String token) {
        logger.info("Verifying email with token: {}", token);
        
        User user = userRepository.findByVerificationToken(token);
        if (user == null) {
            logger.warn("Invalid verification token: {}", token);
            return false;
        }
        
        // Check if token has expired
        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            logger.warn("Verification token expired for user: {}", user.getUsername());
            return false;
        }
        
        // Verify the user
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setVerificationToken(null); // Clear the token
        user.setTokenExpiry(null);
        
        userRepository.save(user);
        logger.info("Email verified successfully for user: {}", user.getUsername());
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
        } catch (Exception e) {
            logger.error("Failed to send welcome email", e);
            // Don't fail the verification for this
        }
        
        return true;
    }
    
    @Transactional
    public boolean resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.isEmailVerified()) {
            return false;
        }
        
        // Generate new token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        
        userRepository.save(user);
        
        try {
            emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), verificationToken);
            logger.info("Verification email resent to: {}", user.getEmail());
            return true;
        } catch (Exception e) {
            logger.error("Failed to resend verification email", e);
            return false;
        }
    }
    
    @Transactional
    public User updateUser(User user) {
        logger.info("Updating user: {}", user.getUsername());
        return userRepository.save(user);
    }
    
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    @Transactional
    public boolean initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            logger.warn("Password reset requested for non-existent email: {}", email);
            return false;
        }
        
        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiry(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        
        userRepository.save(user);
        
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), resetToken);
            logger.info("Password reset email sent to: {}", email);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send password reset email", e);
            return false;
        }
    }
    
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token);
        if (user == null) {
            logger.warn("Invalid password reset token: {}", token);
            return false;
        }
        
        // Check if token has expired
        if (user.getPasswordResetExpiry().isBefore(LocalDateTime.now())) {
            logger.warn("Password reset token expired for user: {}", user.getUsername());
            return false;
        }
        
        // Update password and clear reset token
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        
        userRepository.save(user);
        logger.info("Password reset successfully for user: {}", user.getUsername());
        
        return true;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
} 