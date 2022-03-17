package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.Resource;
import com.spd.trello.domain.items.CheckableItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "checklists")
public class CheckList extends Resource {
    private String name;

    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "checkList")
    @JsonIgnoreProperties("checkList")
    private Set<CheckableItem> items = new LinkedHashSet<>();
}
