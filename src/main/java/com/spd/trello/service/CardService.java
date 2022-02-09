package com.spd.trello.service;

import com.spd.trello.domain.resources.Card;
import com.spd.trello.repository_jpa.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CardService extends AbstractService<Card, CardRepository> {

    public CardService(CardRepository repository) {
        super(repository);
    }

    public List<Card> findAllByCardList(UUID id){
        return repository.findAllByCardListId(id);
    }
}
