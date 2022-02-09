package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.Resource;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Card;
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

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ElementCollection
    @CollectionTable(name = "cards",
            joinColumns = @JoinColumn(name = "cardlist_id"))
    @Column(name = "id")
    private List<UUID> cards = new ArrayList<>();

    private Boolean archived = false;
}
