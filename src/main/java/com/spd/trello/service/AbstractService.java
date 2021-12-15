package com.spd.trello.service;

import com.spd.trello.domain.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractService<T extends Resource> {
    protected List<T> items = new ArrayList();

    public abstract T create(boolean addToList);

    public abstract T update(int index, T obj);

//    public abstract T readByIndex(int index);

//    public abstract T delete(int index);

    public void show() {
        if (items.size() != 0)
            System.out.println(items.get(items.size() - 1));
        else
            System.out.println("You don`t create any item");
    }

    public void showItems() {
        System.out.println(items);
    }
}
