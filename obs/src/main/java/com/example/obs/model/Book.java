package com.example.obs.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String genre;
    private String imageUrl;
    private BigDecimal price;
    private boolean comingSoon; // recommend to use enum for adding more categories in the future
    
    @Column(length = 2000)
    private String description;
    
    // New promotion fields
    private boolean isOnSale = false;
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    public Book() {}

    public Book(Long id, String title, String author, String genre, String imageUrl, BigDecimal price, boolean comingSoon) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.price = price;
        this.comingSoon = comingSoon;
    }

    // Getter / Setter

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isComingSoon() { return comingSoon; }
    public void setComingSoon(boolean comingSoon) { this.comingSoon = comingSoon; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    // New promotion getters/setters
    public boolean isOnSale() { return isOnSale; }
    public void setOnSale(boolean onSale) { isOnSale = onSale; }
    
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    
    // Helper methods for pricing
    public BigDecimal getOriginalPrice() {
        return price;
    }
    
    public BigDecimal getDiscountedPrice() {
        if (isOnSale && discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = price.multiply(discountPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return price.subtract(discount);
        }
        return price;
    }
    
    public BigDecimal getSavingsAmount() {
        return getOriginalPrice().subtract(getDiscountedPrice());
    }
}
