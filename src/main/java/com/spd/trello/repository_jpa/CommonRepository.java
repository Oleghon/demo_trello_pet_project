package com.spd.trello.repository_jpa;

import com.spd.trello.domain.Domain;
import com.spd.trello.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface CommonRepository<E extends Domain> extends JpaRepository<E, UUID>,
        JpaSpecificationExecutor<E> {
    void deleteById(UUID id);

}
