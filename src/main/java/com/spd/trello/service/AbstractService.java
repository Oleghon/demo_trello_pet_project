package com.spd.trello.service;

import com.spd.trello.domain.Resource;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractService<E extends Resource, R extends CommonRepository<E>> implements CommonService<E> {

    R repository;

    @Autowired
    Validator validator;

    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E create(E entity) {

        Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder(entity.getClass().getSimpleName()).append(" not valid");
            throw new ConstraintViolationException(message.toString(), violations);
        } else {
            entity.setCreatedDate(LocalDateTime.now());
            return repository.save(entity);
        }
    }

    @Override
    public E update(E entity) {
        readById(entity.getId());
        entity.setUpdatedDate(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public E readById(UUID id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public E delete(UUID id) {
        E result = readById(id);
        repository.deleteById(id);
        return result;
    }

    @Override
    public Page<E> readAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
