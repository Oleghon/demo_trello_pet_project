package com.spd.trello.domain.resources;

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
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(
            name = "space_member",
            joinColumns = @JoinColumn(name = "space_id"))
    @Column(name = "member_id")
    private Set<UUID> workspaceMembers = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private WorkSpaceVisibility visibility = WorkSpaceVisibility.PUBLIC;

}
