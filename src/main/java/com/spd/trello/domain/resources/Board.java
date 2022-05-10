package com.spd.trello.domain.resources;

import com.spd.trello.domain.ArchivedResource;
import com.spd.trello.domain.enums.BoardVisibility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "boards")
public class Board extends ArchivedResource {
    private String name;
    private String description;

    @Column(name = "workspace_id")
    private UUID workspaceId;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(
            name = "board_member",
            joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "member_id")
    @UniqueElements(message = "member cannot be added twice")
    @NotEmpty(message = "board must contain at least one member")
    private List<UUID> members = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BoardVisibility visibility = BoardVisibility.PUBLIC;

}
