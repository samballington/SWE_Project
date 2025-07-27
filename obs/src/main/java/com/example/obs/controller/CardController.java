package com.example.obs.controller;

import com.example.obs.model.Card;
import com.example.obs.model.User;
import com.example.obs.service.CardService;
import com.example.obs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final UserService userService;

    //add new card (max 4 per user)
    @PostMapping
    public ResponseEntity<?> addCard(@RequestBody Card card, @RequestParam Long userId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        if (cardService.countCardsByUser(userId) >= 4){
            return ResponseEntity.badRequest().body("You can only add 4 cards");
        }
        card.setUser(user);
        Card savedCard = cardService.saveCard(card);
        return ResponseEntity.ok(savedCard);
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<Card>> getCardsByUser(@PathVariable Long userId) {
        if (userService.getUserById(userId) == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cardService.getCardsByUser(userId));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.ok("Card deleted successfully");
    }
}
