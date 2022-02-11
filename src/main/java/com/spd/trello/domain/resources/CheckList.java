package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.Resource;
import com.spd.trello.domain.items.CheckableItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "checklists")
public class CheckList extends Resource {
    private String name;

    @Column(name = "card_id")
    private UUID cardId;
    @OneToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "checkList")
    @JsonIgnoreProperties("checkList")
    private List<CheckableItem> items = new ArrayList<>();
}
