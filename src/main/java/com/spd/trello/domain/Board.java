package com.spd.trello.domain;

import lombok.Data;
import java.util.List;

@Data
public class Board {
    private String name;
    private String description;
    private List<CardList> cardList;
    private List<Member> members;
    private Boolean archived;
    private BoardVisibility visibility;

}
