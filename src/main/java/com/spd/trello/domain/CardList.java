package com.spd.trello.domain;

import java.util.List;

public class CardList {
    private String name;
    private List<Card> cards;
    private boolean isArchived;

    public CardList(String name, List<Card> cards, boolean isArchived) {
        this.name = name;
        this.cards = cards;
        this.isArchived = isArchived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
