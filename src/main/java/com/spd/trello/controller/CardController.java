package com.spd.trello.controller;

import com.spd.trello.domain.resources.Card;
import com.spd.trello.service.CardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
public class CardController extends AbstractController<Card, CardService>{

    public CardController(CardService service) {
        super(service);
    }
}
