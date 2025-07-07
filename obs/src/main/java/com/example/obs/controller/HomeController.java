package com.example.obs.controller;

import com.example.obs.model.Book;
import com.example.obs.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String genre, Model model) {
        List<Book> allBooks;
        
        // Filter by genre if provided
        if (genre != null && !genre.trim().isEmpty()) {
            allBooks = bookService.searchBooksByGenre(genre);
        } else {
            allBooks = bookService.getAllBooks();
        }
        
        // Categorize books
        List<Book> availableBooks = allBooks.stream()
            .filter(book -> !book.isComingSoon())
            .collect(Collectors.toList());
            
        List<Book> comingSoonBooks = allBooks.stream()
            .filter(Book::isComingSoon)
            .collect(Collectors.toList());
            
        // Featured books could be on sale books or first few available books
        List<Book> featuredBooks = allBooks.stream()
            .filter(book -> book.isOnSale() || (!book.isComingSoon() && allBooks.indexOf(book) < 2))
            .collect(Collectors.toList());
        
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("comingSoonBooks", comingSoonBooks);
        model.addAttribute("featuredBooks", featuredBooks);
        model.addAttribute("selectedGenre", genre); // To keep dropdown selection
        
        return "home";
    }
}
