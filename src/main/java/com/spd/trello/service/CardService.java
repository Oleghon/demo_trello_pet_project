package com.spd.trello.service;

import com.spd.trello.domain.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardService extends AbstractService<Card> {
    private static Scanner scanner = new Scanner(System.in);
    private List<Card> cards = new ArrayList<>();

    @Override
    public Card create(boolean addToList) {
        Card card = new Card();
        System.out.println("Input name of card:");
        card.setName(scanner.nextLine());
        System.out.println("Input description of card:");
        card.setDescription(scanner.nextLine());
        return card;
    }

    @Override
    public Card update(int index, Card obj) {
        Card cardForUpdate = cards.get(index);
        cardForUpdate.setName(obj.getName());
        cardForUpdate.setDescription(obj.getDescription());
        return cardForUpdate;
    }
}
