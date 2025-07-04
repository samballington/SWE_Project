package com.example.obs;

import com.example.obs.model.Book;
import com.example.obs.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(BookRepository bookRepository) {
        return (args) -> {
            bookRepository.save(new Book(null, "Book Name", "Writer Name",
                    "Fantasy", "/images/book.jpg", 10.99, false));

            bookRepository.save(new Book(null, "Book Name", "Writer Name",
                    "Fantasy", "/images/book.jpg", 11.99, true));
        };
    }
}
