package com.spd.trello.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CardList {
    private String name;
    private List<Card> cards = new ArrayList<>();
    private Boolean archived = false;
}
