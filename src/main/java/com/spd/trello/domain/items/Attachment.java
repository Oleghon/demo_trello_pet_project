package com.spd.trello.domain.items;

import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.File;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "attachments")
@Entity
public class Attachment extends Domain {
    private String link;
    private String name;
    private File file;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
