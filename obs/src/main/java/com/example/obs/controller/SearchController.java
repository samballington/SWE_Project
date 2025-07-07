package com.example.obs.controller;

import com.example.obs.model.Book;
import com.example.obs.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private BookService bookService;

    // search function should also include search by author or/and genre
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, 
                        @RequestParam(required = false) String genre, 
                        Model model) {
        List<Book> result;
        
        if ((keyword == null || keyword.trim().isEmpty()) && (genre == null || genre.trim().isEmpty())) {
            result = bookService.getAllBooks();
        } else if (genre != null && !genre.trim().isEmpty() && (keyword == null || keyword.trim().isEmpty())) {
            result = bookService.searchBooksByGenre(genre);
        } else if (keyword != null && !keyword.trim().isEmpty() && (genre == null || genre.trim().isEmpty())) {
            result = bookService.searchBooksByTitle(keyword);
        } else {
            result = bookService.searchBooksByTitleAndGenre(keyword, genre);
        }
        
        model.addAttribute("books", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);
        return "search";
    }
}
