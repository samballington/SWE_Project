package com.example.obs.repository;

import com.example.obs.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long>{
} 