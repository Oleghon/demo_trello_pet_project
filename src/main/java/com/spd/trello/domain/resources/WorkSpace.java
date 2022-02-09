package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.Resource;
import com.spd.trello.domain.enums.WorkSpaceVisibility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "workspaces")
public class WorkSpace extends Resource {

    private String name;
    private String description;

    @ElementCollection
    @CollectionTable(name = "boards",
    joinColumns = @JoinColumn(name = "workspace_id"))
    @Column(name = "id")
    private List<UUID> boardList = new ArrayList<>();

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(
            name = "space_member",
            joinColumns = @JoinColumn(name = "space_id"))
    @Column(name = "member_id")
    private Set<UUID> workspaceMembers = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private WorkSpaceVisibility visibility = WorkSpaceVisibility.PUBLIC;

}
