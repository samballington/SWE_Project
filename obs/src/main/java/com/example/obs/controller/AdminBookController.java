package com.example.obs.controller;

import com.example.obs.model.Book;
import com.example.obs.service.BookService;
import com.example.obs.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminBookController {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @GetMapping
    public String adminHome() {
        return "redirect:/admin/books";
    }
    
    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/books";
    }
    
    @GetMapping("/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/book-form";
    }
    
    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/admin/books";
        }
        model.addAttribute("book", book);
        return "admin/book-form";
    }
    
    @PostMapping("/books/save")
    public String saveBook(
            @RequestParam(required = false) Long id,
            @RequestParam String title, 
            @RequestParam String author,
            @RequestParam String genre,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) boolean comingSoon,
            @RequestParam(required = false) MultipartFile imageFile) {
        
        // Create or update the book
        Book book;
        if (id != null) {
            book = bookService.getBookById(id);
            if (book == null) {
                book = new Book();
            }
        } else {
            book = new Book();
        }
        
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setPrice(price);
        book.setComingSoon(comingSoon);
        
        // Save the book first to get an ID if it's new
        Book savedBook = bookService.saveBook(book);
        
        // Handle file upload if present
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(imageFile, savedBook.getId().toString());
            savedBook.setImageUrl("/book-images/" + fileName);
            bookService.saveBook(savedBook);
        }
        
        return "redirect:/admin/books";
    }
    
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }
} 