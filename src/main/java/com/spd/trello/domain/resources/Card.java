package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.Resource;
import com.spd.trello.domain.items.Attachment;
import com.spd.trello.domain.items.Reminder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cards")
public class Card extends Resource {
    private String name;
    private String description;
    private Boolean archived = false;

    @Column(name = "cardlist_id")
    private UUID cardListId;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(
            name = "card_member",
            joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "member_id")
    private List<UUID> assignedMembers = new ArrayList<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "label_card",
            joinColumns = @JoinColumn(name = "card_id"))
    @Column(name="label_id")
    private List<UUID> labels;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "card")
    @JsonIgnoreProperties("card")
    private Set<Attachment> attachments = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "reminder_id", referencedColumnName = "id")
    @JsonIgnoreProperties("card")
    private Reminder reminder;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "checklist_id", referencedColumnName = "id")
    @JsonIgnoreProperties("card")
    private CheckList checkList;
}
