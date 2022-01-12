package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Card extends Resource{
    private String name;
    private String description;
    private CardList cardList;
    private Boolean archived = false;
    private List<Member> assignedMembers = new ArrayList<>();
    private List<Comment> comments;
    private List<Label> labels;
    private Reminder reminder;
    private List<CheckList> checkList;

}
