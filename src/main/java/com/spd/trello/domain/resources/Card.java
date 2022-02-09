package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cards")
public class Card extends Resource {
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "cardlist_id")
    private CardList cardList;

    private Boolean archived = false;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(
            name = "card_member",
            joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "member_id")
    private List<UUID> assignedMembers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "comments",
            joinColumns = @JoinColumn(name = "card_id")
    )
    @Column(name = "id")
    private List<UUID> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "label_card",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<Label> labels;

    @ElementCollection
    @CollectionTable(
            name = "checklists",
            joinColumns = @JoinColumn(name = "card_id")
    )
    @Column(name = "id")
    private List<UUID> checkList;
}
