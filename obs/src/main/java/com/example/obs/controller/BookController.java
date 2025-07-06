package com.example.obs.controller;

import com.example.obs.model.Book;
import com.example.obs.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;
    
    @GetMapping("/book/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        
        if (book == null) {
            return "redirect:/";
        }
        
        model.addAttribute("book", book);
        return "book-details";
    }
} 