package com.example.obs.service;

import com.example.obs.model.Book;
import com.example.obs.model.CartItem;
import com.example.obs.model.User;
import com.example.obs.repository.BookRepository;
import com.example.obs.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;
    
    // In-memory storage for anonymous users' carts
    private final Map<String, List<CartItem>> anonymousCarts = new ConcurrentHashMap<>();

    public void addToCart(User user, Book book, int quantity) {
        if (user.getId() != null) {
            // Registered user - use database
            Optional<CartItem> existing = cartItemRepository.findByUserId(user).stream()
                    .filter(item -> item.getBookId().equals(book))
                    .findFirst();

            if (existing.isPresent()) {
                CartItem item = existing.get();
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepository.save(item);
            } else {
                cartItemRepository.save(new CartItem(user, book, quantity));
            }
        } else {
            // Anonymous user - use session
            String sessionId = user.getUsername(); // Using the generated anonymous ID as session ID
            List<CartItem> sessionCart = anonymousCarts.computeIfAbsent(sessionId, k -> new ArrayList<>());
            
            // Check if book already exists in cart
            Optional<CartItem> existing = sessionCart.stream()
                    .filter(item -> item.getBookId().getId().equals(book.getId()))
                    .findFirst();
                    
            if (existing.isPresent()) {
                CartItem item = existing.get();
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                sessionCart.add(new CartItem(user, book, quantity));
            }
        }
    }

    public List<Map<String, Object>> getCartItems(User user) {
        List<CartItem> cartItems;
        
        if (user.getId() != null) {
            // Registered user - get from database
            cartItems = cartItemRepository.findByUserId(user);
        } else {
            // Anonymous user - get from session
            String sessionId = user.getUsername();
            cartItems = anonymousCarts.getOrDefault(sessionId, new ArrayList<>());
        }
        
        List<Map<String, Object>> result = new ArrayList<>();

        for (CartItem item : cartItems) {
            Book book = item.getBookId();
            if (book != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("book", book);
                map.put("title", book.getTitle());
                map.put("price", book.getPrice());
                map.put("quantity", item.getQuantity());
                map.put("subtotal", book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                result.add(map);
            }
        }

        return result;
    }
    
    @Transactional
    public void clearCart(User user) {
        if (user.getId() != null) {
            // Registered user - clear from database
            cartItemRepository.deleteByUserId(user);
        } else {
            // Anonymous user - clear from session
            String sessionId = user.getUsername();
            anonymousCarts.remove(sessionId);
        }
    }
    
    public int getCartItemCount(User user) {
        if (user.getId() != null) {
            // Registered user - count from database
            List<CartItem> items = cartItemRepository.findByUserId(user);
            return items.stream().mapToInt(CartItem::getQuantity).sum();
        } else {
            // Anonymous user - count from session
            String sessionId = user.getUsername();
            List<CartItem> items = anonymousCarts.getOrDefault(sessionId, new ArrayList<>());
            return items.stream().mapToInt(CartItem::getQuantity).sum();
        }
    }
}
