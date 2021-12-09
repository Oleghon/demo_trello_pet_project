package com.spd.trello.domain;

import java.util.List;

public class Board {
    private String name;
    private String description;
    private List<CardList> cardList;
    private List<Member> members;
    private boolean favoriteStatus;
    private boolean isArchived;
    private BoardVisibility visibility;

}
