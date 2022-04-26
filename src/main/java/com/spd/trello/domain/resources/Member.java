package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spd.trello.domain.Resource;
import com.spd.trello.domain.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "members")
public class Member extends Resource {

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private Role role = Role.GUEST;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(
            name = "space_member",
            joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "space_id")
    @JsonIgnore
    private Set<UUID> workspaces = new LinkedHashSet<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.TRUE)
    @CollectionTable(
            name = "board_member",
            joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "board_id")
    @JsonIgnore
    private Set<UUID> boards = new LinkedHashSet<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.TRUE)
    @CollectionTable(
            name = "card_member",
            joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "card_id")
    @JsonIgnore
    private Set<UUID> cards = new LinkedHashSet<>();


}
