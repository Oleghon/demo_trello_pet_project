package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.items.Attachment;
import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comments")
public class Comment extends Resource {
    private String text;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE,
            mappedBy = "comment")
    @JsonIgnoreProperties("comment")
    private List<Attachment> attachments;
}
