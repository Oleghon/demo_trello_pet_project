package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckList extends Resource{
    private String name;
    private List<CheckableItem> items = new ArrayList<>();
}
