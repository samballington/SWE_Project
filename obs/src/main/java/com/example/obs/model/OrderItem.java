package com.example.obs.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private int quantity;
    private BigDecimal price;

    public OrderItem(){}

    public OrderItem(Order order, Book book, int quantity, BigDecimal price){
        this.order=order;
        this.book=book;
        this.quantity=quantity;
        this.price=price;
    }

    // getters setters
    public Long getId(){return id;}
    public Order getOrder(){return order;}
    public void setOrder(Order order){this.order=order;}
    public Book getBook(){return book;}
    public void setBook(Book book){this.book=book;}
    public int getQuantity(){return quantity;}
    public void setQuantity(int q){this.quantity=q;}
    public BigDecimal getPrice(){return price;}
    public void setPrice(BigDecimal p){this.price=p;}
} 