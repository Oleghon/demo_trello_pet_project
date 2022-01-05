package com.spd.trello.service;

import com.spd.trello.domain.Resource;

import java.util.List;
import java.util.UUID;

public abstract class AbstractService<T extends Resource> {

    public abstract T create(T obj);

    public abstract T update(UUID id, T obj);

    public abstract T readById(UUID id);

    public abstract boolean delete(UUID id);

    public abstract List<T> readAll();
}
