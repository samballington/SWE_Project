package com.example.obs.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

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
}
