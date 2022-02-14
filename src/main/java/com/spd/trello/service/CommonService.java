package com.spd.trello.service;

import com.spd.trello.domain.Resource;

import java.util.List;
import java.util.UUID;

public interface CommonService<E extends Resource> {
    E create(E entity);

    E update(E entity);

    E readById(UUID id);

    E delete(UUID id);

    List<E> readAll();
}
