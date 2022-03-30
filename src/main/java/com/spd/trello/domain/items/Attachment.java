package com.spd.trello.domain.items;

import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.Card;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "attachments")
@Entity
public class Attachment extends Domain {
    private String context;
    private String name;
    @Lob
    private byte[] file;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "card_id", nullable= true, referencedColumnName = "id")
//    private Card card;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "comment_id", nullable = true, referencedColumnName = "id")
//    @JsonIgnore
//    private Comment comment;
}
