package com.example.obs.repository;

import com.example.obs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByVerificationToken(String verificationToken);
    User findByPasswordResetToken(String passwordResetToken);

} 