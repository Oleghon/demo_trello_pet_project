package com.spd.trello.controller;

import com.spd.trello.domain.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.UUID;

public interface IController<E extends Resource> {

    ResponseEntity<E> create(E resource, Principal principal);

    ResponseEntity<E> update(UUID id, E resource, Principal principal);

    ResponseEntity<E> readById(UUID id);

    ResponseEntity<E> delete(UUID id);

    Page<E> readAll(Pageable pageable);
}
