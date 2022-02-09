package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            mappedBy = "checkList")
    @JsonIgnoreProperties("checkList")
    private List<CheckableItem> items = new ArrayList<>();
}
