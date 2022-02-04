package com.spd.trello.controller;

import com.spd.trello.domain.resources.CardList;
import com.spd.trello.service.CardListService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cardlists")
public class CardListController extends AbstractController<CardList, CardListService>{
    public CardListController(CardListService service) {
        super(service);
    }
}
