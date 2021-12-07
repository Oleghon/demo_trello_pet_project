package com.spd.domain;

import java.util.List;

public class CheckList {
    private String name;
    private List<CheckableItem> items;

    public CheckList() {}

    public CheckList(String name, List<CheckableItem> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CheckableItem> getItems() {
        return items;
    }

    public void setItems(List<CheckableItem> items) {
        this.items = items;
    }
}
