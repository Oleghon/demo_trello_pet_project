package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Label extends Resource {
    private String colorName;
}
