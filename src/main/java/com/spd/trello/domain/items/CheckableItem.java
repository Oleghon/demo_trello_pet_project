package com.spd.trello.domain.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.CheckList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "items")
public class CheckableItem extends Domain {
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "checklist_id", referencedColumnName = "id")
    @JsonIgnoreProperties("checkListItem")
    private CheckList checkList;
    private String name;
    @Column(name = "checked")
    private Boolean check = false;
}
