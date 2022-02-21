package com.spd.trello.domain.items;

import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.File;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "attachments")
@Entity
public class Attachment extends Domain {
    private String link;
    private String name;
    private File file;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable= true, referencedColumnName = "id")
    private Card card;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id", nullable = true, referencedColumnName = "id")
    private Comment comment;
}
