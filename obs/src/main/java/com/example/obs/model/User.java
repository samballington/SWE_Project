package com.example.obs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;
    
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @Column(name = "first_name")
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @Column(name = "last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phone;
    
    @Column(length = 500)
    private String address;
    
    @Column(name = "payment_info", length = 500)
    private String paymentInfo;
    
    @Column(name = "user_role")
    private String role = "USER";
    
    private boolean enabled = true;
    
    // Email verification fields
    @Column(name = "email_verified")
    private boolean emailVerified = false;
    
    @Column(name = "verification_token")
    private String verificationToken;
    
    @Column(name = "token_expiry")
    private java.time.LocalDateTime tokenExpiry;
    
    // Password reset fields
    @Column(name = "password_reset_token")
    private String passwordResetToken;
    
    @Column(name = "password_reset_expiry")
    private java.time.LocalDateTime passwordResetExpiry;
    
    // Promotions signup
    @Column(name = "promotions_subscribed")
    private Boolean promotionsSubscribed = false;
}