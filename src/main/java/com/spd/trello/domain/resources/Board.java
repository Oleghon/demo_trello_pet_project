package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import com.spd.trello.domain.enums.BoardVisibility;
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
@Table(name = "boards")
public class Board extends Resource {
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private WorkSpace workSpace;

    @ElementCollection
    @CollectionTable(name = "cardlists",
            joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "id")
    private List<UUID> cardLists = new ArrayList<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(
            name = "board_member",
            joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "member_id")
    private List<UUID> members = new ArrayList<>();

    private Boolean archived = false;

    @Enumerated(EnumType.STRING)
    private BoardVisibility visibility = BoardVisibility.PUBLIC;

}
