package com.example.obs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public void sendVerificationEmail(String toEmail, String username, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setFrom(fromEmail);
            message.setSubject("Email Verification - Online Book Store");
            
            String verificationUrl = baseUrl + "/verify-email?token=" + verificationToken;
            
            String emailBody = String.format(
                "Hello %s,\n\n" +
                "Thank you for registering with our Online Book Store!\n\n" +
                "Please click the link below to verify your email address:\n" +
                "%s\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you didn't create this account, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Online Book Store Team",
                username, verificationUrl
            );
            
            message.setText(emailBody);
            
            mailSender.send(message);
            logger.info("Verification email sent successfully to: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Failed to send verification email to: " + toEmail, e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setFrom(fromEmail);
            message.setSubject("Welcome to Online Book Store!");
            
            String emailBody = String.format(
                "Hello %s,\n\n" +
                "Welcome to our Online Book Store! Your email has been successfully verified.\n\n" +
                "You can now:\n" +
                "- Browse our extensive book collection\n" +
                "- Add books to your cart\n" +
                "- Place orders securely\n" +
                "- Manage your profile\n\n" +
                "Start exploring: %s\n\n" +
                "Happy reading!\n\n" +
                "Best regards,\n" +
                "Online Book Store Team",
                username, baseUrl
            );
            
            message.setText(emailBody);
            
            mailSender.send(message);
            logger.info("Welcome email sent successfully to: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Failed to send welcome email to: " + toEmail, e);
            // Don't throw exception for welcome email failure
        }
    }
    
    public void sendPasswordResetEmail(String toEmail, String username, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setFrom(fromEmail);
            message.setSubject("Password Reset - Online Book Store");
            
            String resetUrl = baseUrl + "/reset-password?token=" + resetToken;
            
            String emailBody = String.format(
                "Hello %s,\n\n" +
                "You requested to reset your password for your Online Book Store account.\n\n" +
                "Please click the link below to reset your password:\n" +
                "%s\n\n" +
                "This link will expire in 1 hour for security reasons.\n\n" +
                "If you didn't request this password reset, please ignore this email. " +
                "Your password will remain unchanged.\n\n" +
                "Best regards,\n" +
                "Online Book Store Team",
                username, resetUrl
            );
            
            message.setText(emailBody);
            
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: " + toEmail, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
} 