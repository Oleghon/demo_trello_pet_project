package com.spd.trello.service;

import com.spd.trello.domain.resources.CardList;
import com.spd.trello.repository_jpa.CardListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CardListService extends AbstractService<CardList, CardListRepository> {

    public CardListService(CardListRepository repository) {
        super(repository);
    }

    public List<CardList> findAllByBoard(UUID id) {
        return repository.findAllByBoardId(id);
    }
}
