package com.example.obs.service;

import com.example.obs.model.Book;
import com.example.obs.model.CartItem;
import com.example.obs.model.User;
import com.example.obs.repository.BookRepository;
import com.example.obs.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    public void addToCart(User userId, Book bookId, int quantity) {
        Optional<CartItem> existing = cartItemRepository.findByUserId(userId).stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.save(new CartItem(userId, bookId, quantity));
        }
    }

    public List<Map<String, Object>> getCartItems(User userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (CartItem item : cartItems) {
            Book book = item.getBookId();
            if (book != null) {
                Map<String, Object> map = new HashMap<>();
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
    public void clearCart(User userId) {
        cartItemRepository.deleteByUserId(userId);
    }
    
    public int getCartItemCount(User userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

}
