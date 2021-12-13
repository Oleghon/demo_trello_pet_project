package com.spd.trello.service;

import com.spd.trello.domain.Card;
import com.spd.trello.domain.CardList;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardListService extends AbstractService<CardList> {
    private static Scanner scanner = new Scanner(System.in);
    private List<CardList> cardLists = new ArrayList<>();

    @Override
    public CardList create() {
        CardList cardList = new CardList();
        cardList.setName(scanner.nextLine());
        System.out.println("Do you want to add cards? Y/N");
        if (scanner.next().equals("Y"))
            cardList.setCards(addCards());
        cardLists.add(cardList);
        return cardList;
    }

    private List<Card> addCards() {
        CardService cardService = new CardService();
        List<Card> cards = new ArrayList<>();
        int mark = 1;
        while (mark == 1) {
            cards.add(cardService.create());
            System.out.println("If you want to add one more card press '1' else press '0' ");
            mark = scanner.nextInt();
        }
        return cards;
    }

    @Override
    public CardList update(int index, CardList obj) {
        CardList cardListForUpdate = cardLists.get(index);
        cardListForUpdate.setName(obj.getName());
        return cardListForUpdate;
    }

}
