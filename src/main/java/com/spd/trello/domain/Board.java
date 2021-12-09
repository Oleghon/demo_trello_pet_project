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

    public Board() {}

    public Board(String name, String description, List<CardList> cardList) {
        this.name = name;
        this.description = description;
        this.cardList = cardList;
    }

    public Board(String name, String description, List<CardList> cardList, List<Member> members,
                 boolean favoriteStatus, boolean isArchived, BoardVisibility visibility) {
        this.name = name;
        this.description = description;
        this.cardList = cardList;
        this.members = members;
        this.favoriteStatus = favoriteStatus;
        this.isArchived = isArchived;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CardList> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardList> cardList) {
        this.cardList = cardList;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public boolean isFavoriteStatus() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public BoardVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(BoardVisibility visibility) {
        this.visibility = visibility;
    }
}
