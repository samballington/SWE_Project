package com.example.obs.repository;

import com.example.obs.model.CartItem;
import com.example.obs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(User userId);
    void deleteByUserId(User userId);
}
