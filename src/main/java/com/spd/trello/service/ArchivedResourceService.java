package com.spd.trello.service;

import com.spd.trello.domain.ArchivedResource;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.CommonRepository;

import java.time.LocalDateTime;

public class ArchivedResourceService<E extends ArchivedResource, R extends CommonRepository<E>>
        extends AbstractService<E, R> {

    public ArchivedResourceService(R repository) {
        super(repository);
    }

    @Override
    public E update(E entity) {
        E foundEntity = repository.findById(entity.getId()).get();
        entity = checkArchivedResource(entity, foundEntity);
        return repository.save(entity);
    }

    private E checkArchivedResource(E entity, E foundEntity) {
        if (foundEntity.getArchived() && entity.getArchived())
            throw new IllegalArgumentException("Archived " + entity.getClass().getSimpleName() + " can not be updated");
        else if ((!foundEntity.getArchived() && entity.getArchived())
                || (foundEntity.getArchived() && !entity.getArchived())) {
            foundEntity.setArchived(entity.getArchived());
            return foundEntity;
        }
        entity.setUpdatedDate(LocalDateTime.now());
        entity.setCreatedDate(foundEntity.getCreatedDate());
        entity.setCreatedBy(foundEntity.getCreatedBy());
        return entity;
    }
}
