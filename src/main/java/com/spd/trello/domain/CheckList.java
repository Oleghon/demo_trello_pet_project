package com.spd.trello.domain;

import lombok.Data;
import java.util.List;

@Data
public class CheckList {
    private String name;
    private List<CheckableItem> items;
}
