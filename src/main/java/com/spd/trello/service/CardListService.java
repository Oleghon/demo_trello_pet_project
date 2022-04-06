package com.spd.trello.service;

import com.spd.trello.domain.resources.CardList;
import com.spd.trello.repository_jpa.CardListRepository;
import org.springframework.stereotype.Service;

@Service
public class CardListService extends ArchivedResourceService<CardList, CardListRepository> {

    public CardListService(CardListRepository repository) {
        super(repository);
    }

}
