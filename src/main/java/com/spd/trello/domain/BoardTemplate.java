package com.spd.trello.domain;

import java.util.List;

public class BoardTemplate extends Board{
    private String title;

    public BoardTemplate(String name, String description, List<CardList> cardList, String title) {
        super(name, description, cardList);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
