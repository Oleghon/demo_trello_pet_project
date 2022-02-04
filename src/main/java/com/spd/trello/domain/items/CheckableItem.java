package com.spd.trello.domain.items;

import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.CheckList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckableItem extends Domain {
    private CheckList checkList;
    private String name;
    private Boolean check = false;
}
