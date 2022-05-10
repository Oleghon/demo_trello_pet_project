package com.spd.trello.service;

import com.spd.trello.annotation.Debug;
import com.spd.trello.domain.Resource;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.CommonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
@Slf4j
public abstract class AbstractService<E extends Resource, R extends CommonRepository<E>> implements CommonService<E> {

    R repository;

    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Debug
    @Override
    public E create(E entity) {
        log.info("entity: {} successfully saved in db", entity);
        return repository.save(entity);
    }

    @Debug
    @Override
    public E update(E entity) {
        readById(entity.getId());
        log.info("entity: {} successfully updated in db", entity);
        return repository.save(entity);
    }

    @Debug
    @Override
    public E readById(UUID id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Debug
    @Override
    public E delete(UUID id) {
        E result = readById(id);
        repository.deleteById(id);
        log.info("entity with id: {} has just been deleted from db", id);
        return result;
    }

    @Debug
    @Override
    public Page<E> readAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
