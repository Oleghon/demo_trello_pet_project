package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Card;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CardList extends Resource {
    private String name;
    private Board board;
    private List<Card> cards = new ArrayList<>();
    private Boolean archived = false;
}
