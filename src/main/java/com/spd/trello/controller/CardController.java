package com.spd.trello.controller;

import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/cards")
public class CardController extends AbstractController<Card, CardService>{

    public CardController(CardService service) {
        super(service);
    }

    @PostMapping("/{id}/add_member")
    public ResponseEntity<Card> addMember(@PathVariable UUID id, @RequestBody Member member){
        Card card = service.addMember(id, member);
        log.info("entity with id: {} has just been added to card: {}", member.getId(), card.getId());
        return new ResponseEntity<>(card, HttpStatus.OK);
    }
}
