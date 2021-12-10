package com.spd.trello.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board extends BoardTemplate{
    private String name;
    private String description;
    private List<CardList> cardLists = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private Boolean archived = false;
    private BoardVisibility visibility = BoardVisibility.PUBLIC;

}
