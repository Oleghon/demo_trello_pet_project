package com.spd.trello.service;

import com.spd.trello.domain.Card;
import com.spd.trello.repository.impl.CardRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CardService extends AbstractService<Card> {

    CardRepositoryImpl cardRepository;

    public CardService() {
        this.cardRepository = new CardRepositoryImpl();
    }

    @Override
    public Card create(Card obj) {

        cardRepository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public Card update(UUID id, Card obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        cardRepository.update(id, obj);
        return readById(id);
    }

    @Override
    public Card readById(UUID id) {
        return cardRepository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return cardRepository.delete(id);
    }

    @Override
    public List<Card> readAll() {
        return cardRepository.getObjects();
    }
}
