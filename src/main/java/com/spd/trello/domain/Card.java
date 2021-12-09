package com.spd.trello.domain;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Card {
    private String name;
    private String description;
    private Boolean archived;
    private LocalDateTime creationDate;
    private List<Member> assignedMembers;
    private List<Comment> comments;
    private List<Label> labels;
    private Reminder reminder;
    private List<CheckList> checkList;

}
