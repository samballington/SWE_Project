package com.example.obs.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    private String shippingAddress;
    private String paymentInfo; // masked card or placeholder
    private String email;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Promo code tracking
    @ManyToOne
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;
    private BigDecimal discount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    // getters and setters omitted for brevity

    public Long getId() {return id;}
    public String getOrderNumber() {return orderNumber;}
    public void setOrderNumber(String orderNumber) {this.orderNumber = orderNumber;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public BigDecimal getSubtotal() {return subtotal;}
    public void setSubtotal(BigDecimal subtotal) {this.subtotal = subtotal;}
    public BigDecimal getTax() {return tax;}
    public void setTax(BigDecimal tax) {this.tax = tax;}
    public BigDecimal getTotal() {return total;}
    public void setTotal(BigDecimal total) {this.total = total;}
    public String getShippingAddress() {return shippingAddress;}
    public void setShippingAddress(String shippingAddress) {this.shippingAddress = shippingAddress;}
    public String getPaymentInfo() {return paymentInfo;}
    public void setPaymentInfo(String paymentInfo) {this.paymentInfo = paymentInfo;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public List<OrderItem> getItems() {return items;}
    
    // Promo code getters and setters
    public PromoCode getPromoCode() { return promoCode; }
    public void setPromoCode(PromoCode promoCode) { this.promoCode = promoCode; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
} 