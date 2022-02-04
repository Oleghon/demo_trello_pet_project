package com.spd.trello.service;

import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.CardList;
import com.spd.trello.repository.impl.CardListRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CardListService extends AbstractService<CardList> {

    private CardListRepositoryImpl cardListRepository;

    @Autowired
    public CardListService(CardListRepositoryImpl cardListRepository) {
        this.cardListRepository = cardListRepository;
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
