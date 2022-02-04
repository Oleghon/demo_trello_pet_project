package com.spd.trello.domain.items;

import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

@Data
@EqualsAndHashCode(callSuper = true)
public class Attachment extends Domain {
    private String link;
    private String name;
    private File file;
    private Card card;
    private Comment comment;
}
