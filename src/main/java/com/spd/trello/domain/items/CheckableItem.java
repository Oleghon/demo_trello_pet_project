package com.spd.trello.domain.items;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.CheckList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.property.access.spi.Getter;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "items")
public class CheckableItem extends Domain {
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checklist_id", referencedColumnName = "id")
    private CheckList checkList;
    private String name;
    @Column(name = "checked")
    private Boolean check = false;
}
