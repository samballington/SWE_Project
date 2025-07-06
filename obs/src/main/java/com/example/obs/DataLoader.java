package com.example.obs;

import com.example.obs.model.Book;
import com.example.obs.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(BookRepository bookRepository) {
        return (args) -> {
            bookRepository.save(new Book(null, "Little Prince", "Antoine",
                    "Classic", "/images/littleprince.jpg", BigDecimal.valueOf(15.99), false));

            bookRepository.save(new Book(null, "Introduction to Algorithms", "Thomas",
                    "Programming", "/images/algorithms.jpg", BigDecimal.valueOf(99.99), true));
        };
    }
}
