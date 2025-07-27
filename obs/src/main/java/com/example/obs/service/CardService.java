package com.example.obs.service;

import com.example.obs.model.Card;
import com.example.obs.repository.CardRepository;
import com.example.obs.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Card saveCard(Card card) {
        card.setCardNumber(EncryptionUtil.encrypt(card.getCardNumber()));
        return cardRepository.save(card);
    }

    public List<Card> getCardsByUser(Long userId) {
        return cardRepository.findByUserId(userId)
                .stream()
                .map(this::decryptCard)
                .collect(Collectors.toList());
    }
    public long countCardsByUser(Long userId) {
        return cardRepository.countByUserId(userId);
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    private Card decryptCard(Card card) {
        String decrypted = EncryptionUtil.decrypt(card.getCardNumber());
        if (decrypted.length() > 4) {
            String masked = "**** **** **** " + decrypted.substring(decrypted.length() - 4);
            card.setCardNumber(masked);
        } else {
            card.setCardNumber(decrypted);
        }
        return card;
    }

}
