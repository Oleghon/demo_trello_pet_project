package com.spd.trello.controller;

import com.spd.trello.domain.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IController<E extends Resource> {

    ResponseEntity<E> create(E resource);

    ResponseEntity<E> update(UUID id, E resource);

    ResponseEntity<E> readById(UUID id);

    ResponseEntity<E> delete(UUID id);

    Page<E> readAll(Pageable pageable);
}
