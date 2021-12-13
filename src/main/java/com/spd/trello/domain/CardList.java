package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CardList extends Resource{
    private String name;
    private List<Card> cards = new ArrayList<>();
    private Boolean archived = false;
}
