package com.spd.trello.domain.resources;

import com.spd.trello.domain.*;
import com.spd.trello.domain.enums.BoardVisibility;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Board extends Resource {
    private String name;
    private String description;
    private WorkSpace workSpace;
    private List<CardList> cardLists = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private Boolean archived = false;
    private BoardVisibility visibility = BoardVisibility.PUBLIC;

}
