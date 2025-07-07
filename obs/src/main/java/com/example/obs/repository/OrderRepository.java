package com.example.obs.repository;

import com.example.obs.model.Order;
import com.example.obs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long>{
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    Order findByIdAndUser(Long id, User user);
    List<Order> findAllByOrderByCreatedAtDesc();
} 