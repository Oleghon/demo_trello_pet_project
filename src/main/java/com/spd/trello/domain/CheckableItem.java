package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckableItem extends Domain {
    private String name;
    private Boolean check = false;
}
