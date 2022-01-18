package com.spd.trello.service;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.CardList;
import com.spd.trello.repository.impl.CardListRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CardListService extends AbstractService<CardList> {

    private CardListRepositoryImpl cardListRepository;

    public CardListService() {
        cardListRepository = new CardListRepositoryImpl();
    }

    public CardList create(String createdBy, String name, boolean arhived, Board board) {
        CardList cardList = new CardList();
        cardList.setCreatedBy(createdBy);
        cardList.setName(name);
        cardList.setArchived(arhived);
        cardList.setBoard(board);
        return create(cardList);
    }

    @Override
    public CardList create(CardList obj) {
        cardListRepository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public CardList update(UUID id, CardList obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        cardListRepository.update(id, obj);
        return readById(id);
    }

    @Override
    public CardList readById(UUID id) {
        return cardListRepository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return cardListRepository.delete(id);
    }

    @Override
    public List<CardList> readAll() {
        return cardListRepository.getObjects();
    }
}
