package com.spd.trello.domain;

import java.util.List;

public class Board {
    private String name;
    private String description;
    private List<CardList> cardList;
    private List<Member> members;
    private Boolean favorite;
    private Boolean archived;
    private BoardVisibility visibility;

}
