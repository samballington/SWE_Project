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

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<Book> result = bookService.searchBooksByTitle(keyword);
        model.addAttribute("books", result);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}
