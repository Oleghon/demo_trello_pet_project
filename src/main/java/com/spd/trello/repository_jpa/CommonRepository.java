package com.spd.trello.repository_jpa;

import com.spd.trello.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface CommonRepository<E extends Resource> extends JpaRepository<E, UUID> {
    void deleteById(UUID id);
}
