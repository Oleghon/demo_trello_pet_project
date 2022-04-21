package com.spd.trello.controller;

import com.spd.trello.domain.Resource;
import com.spd.trello.service.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


public class AbstractController<E extends Resource, S extends CommonService<E>>
        implements IController<E> {

    S service;

    public AbstractController(S service) {
        this.service = service;
    }

    @PostMapping
    @Override
    @PreAuthorize("hasAnyAuthority('write', 'workspace:write')")
    public ResponseEntity<E> create(@RequestBody @Valid E resource) {
        E result = service.create(resource);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Override
    @PreAuthorize("hasAuthority('update')")
    public ResponseEntity<E> update(@PathVariable UUID id, @RequestBody @Valid E resource) {
        E result = service.update(resource);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Override
    @PreAuthorize("hasAuthority('read')")
    public ResponseEntity<E> readById(@PathVariable UUID id) {
        E result = service.readById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Override
    @PreAuthorize("hasAuthority('write')")
    public ResponseEntity<E> delete(@PathVariable UUID id) {
        E result = service.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    @Override
    @PreAuthorize("hasAuthority('read')")
    public Page<E> readAll(Pageable pageable) {
        return service.readAll(pageable);
    }
}
