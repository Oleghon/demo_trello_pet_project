package com.spd.trello.service;

import com.spd.trello.domain.resources.Card;
import com.spd.trello.repository_jpa.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService extends ArchivedResourceService<Card, CardRepository> {

    public CardService(CardRepository repository) {
        super(repository);
    }

}
