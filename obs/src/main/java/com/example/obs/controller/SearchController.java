package com.example.obs.controller;

import com.example.obs.model.Book;
import com.example.obs.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, 
                        @RequestParam(required = false) String genre, 
                        Model model) {
        List<Book> result = new ArrayList<>();
        
        if ((keyword == null || keyword.trim().isEmpty()) && (genre == null || genre.trim().isEmpty())) {
            // No search criteria - show all books
            result = bookService.getAllBooks();
        } else if (genre != null && !genre.trim().isEmpty() && (keyword == null || keyword.trim().isEmpty())) {
            // Genre filter only
            result = bookService.searchBooksByGenre(genre);
        } else if (keyword != null && !keyword.trim().isEmpty() && (genre == null || genre.trim().isEmpty())) {
            // Keyword search only - search both title and author
            Set<Book> combinedResults = new HashSet<>();
            combinedResults.addAll(bookService.searchBooksByTitle(keyword));
            combinedResults.addAll(bookService.searchBooksByAuthor(keyword));
            result = new ArrayList<>(combinedResults);
        } else {
            // Both keyword and genre specified
            Set<Book> combinedResults = new HashSet<>();
            combinedResults.addAll(bookService.searchBooksByTitleAndGenre(keyword, genre));
            // Also search by author with genre filter
            List<Book> authorResults = bookService.searchBooksByAuthor(keyword);
            List<Book> authorGenreResults = authorResults.stream()
                .filter(book -> book.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
            combinedResults.addAll(authorGenreResults);
            result = new ArrayList<>(combinedResults);
        }
        
        // Sort results by title for consistent ordering
        result.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        
        model.addAttribute("books", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);
        return "search";
    }
}
