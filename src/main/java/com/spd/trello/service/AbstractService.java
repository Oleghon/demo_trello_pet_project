package com.spd.trello.service;

public abstract class AbstractService<T> {
    public abstract T create();

    public abstract T update(int index, T obj);

//    public abstract T readByIndex(int index);

//    public abstract T delete(int index);

    public void show(T obj) {
        System.out.println(obj);
    }

}
