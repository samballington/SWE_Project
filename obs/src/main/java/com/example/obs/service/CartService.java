package com.example.obs.service;

import com.example.obs.model.Book;
import com.example.obs.model.CartItem;
import com.example.obs.repository.BookRepository;
import com.example.obs.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    public void addToCart(Long userId, Long bookId, int quantity) {
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

    public List<Map<String, Object>> getCartItems(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (CartItem item : cartItems) {
            Optional<Book> book = bookRepository.findById(item.getBookId());
            if (book.isPresent()) {
                Map<String, Object> map = new HashMap<>();
                map.put("title", book.get().getTitle());
                map.put("price", book.get().getPrice());
                map.put("quantity", item.getQuantity());
                map.put("subtotal", book.get().getPrice() * item.getQuantity());
                result.add(map);
            }
        }

        return result;
    }
    
    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
    
    public int getCartItemCount(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

}
