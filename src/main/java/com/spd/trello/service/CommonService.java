package com.spd.trello.service;

import com.spd.trello.domain.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CommonService<E extends Resource> {
    E create(E entity);

    E update(E entity);

    E readById(UUID id);

    E delete(UUID id);

    Page<E> readAll(Pageable pageable);
}
