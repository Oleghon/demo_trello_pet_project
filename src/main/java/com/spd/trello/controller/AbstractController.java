package com.spd.trello.controller;

import com.spd.trello.domain.Resource;
import com.spd.trello.service.AbstractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public class AbstractController<E extends Resource, S extends AbstractService<E>>
        implements IController<E> {

    S service;

    public AbstractController(S service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public ResponseEntity<E> create(@RequestBody E resource) {
        E result = service.create(resource);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<E> update(@PathVariable UUID id, @RequestBody E resource) {
        E result = service.update(id, resource);
        //todo
        return new ResponseEntity<>(result, HttpStatus.RESET_CONTENT);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<E> readById(@PathVariable UUID id) {
        E result = service.readById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Override
    public HttpStatus delete(@PathVariable UUID id) {
        return service.delete(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }

    @GetMapping
    @Override
    public List<E> readAll() {
        return service.readAll();
    }
}
