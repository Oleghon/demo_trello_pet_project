package com.spd.trello.domain;

import java.time.LocalDateTime;
import java.util.List;

public class CardTemplate extends Card{
    private String title;

    public CardTemplate(String name, String description, boolean isArchived,
                        LocalDateTime creationDate, List<Member> assignedMembers, String title) {
        super(name, description, isArchived, creationDate, assignedMembers);
        this.title = title;
    }

    public CardTemplate(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
