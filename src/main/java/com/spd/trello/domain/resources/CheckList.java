package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spd.trello.domain.Resource;
import com.spd.trello.domain.items.CheckableItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "checklists")
public class CheckList extends Resource {
    private String name;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            targetEntity = CheckableItem.class,
            mappedBy = "checkList")
    @JsonManagedReference
    @JsonIgnoreProperties(value = "checkList", allowSetters = true)
    private List<CheckableItem> items = new ArrayList<>();
}
