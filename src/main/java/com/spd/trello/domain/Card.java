package com.spd.trello.domain;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Card extends CardTemplate{
    private String name;
    private String description;
    private Boolean archived = false;
    private LocalDateTime creationDate = LocalDateTime.now();
    private List<Member> assignedMembers = new ArrayList<>();
    private List<Comment> comments;
    private List<Label> labels;
    private Reminder reminder;
    private List<CheckList> checkList;

}
