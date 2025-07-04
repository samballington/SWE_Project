package com.example.obs.model;

import jakarta.persistence.*;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String genre;
    private String imageUrl;
    private double price;
    private boolean comingSoon;

    public Book() {}

    public Book(Long id, String title, String author, String genre, String imageUrl, double price, boolean comingSoon) {
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

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isComingSoon() { return comingSoon; }
    public void setComingSoon(boolean comingSoon) { this.comingSoon = comingSoon; }
}
