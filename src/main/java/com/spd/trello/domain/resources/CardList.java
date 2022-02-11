package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cardlists")
public class CardList extends Resource {
    private String name;

    @Column(name = "board_id")
    private UUID boardId;

    @ElementCollection
    @CollectionTable(name = "cards",
            joinColumns = @JoinColumn(name = "cardlist_id"))
    @Column(name = "id")
    @JsonIgnore
    private List<UUID> cards = new ArrayList<>();

    private Boolean archived = false;
}
