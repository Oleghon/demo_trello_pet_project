package com.spd.trello.controller;

import com.spd.trello.domain.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IController<E extends Resource> {

    ResponseEntity<E> create(E resource);

    ResponseEntity<E> update(UUID id, E resource);

    ResponseEntity<E> readById(UUID id);

    ResponseEntity<E> delete(UUID id);

    List<E> readAll();
}
