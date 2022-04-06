package com.spd.trello.domain.resources;

import com.spd.trello.domain.ArchivedResource;
import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cardlists")
public class CardList extends ArchivedResource {
    private String name;
    private UUID boardId;
}
