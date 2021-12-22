package com.spd.trello.repository;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Domain;

import java.util.UUID;

public interface Repository<T extends Domain> {

    JdbcConfig config = new JdbcConfig();

    T create(T obj);

    T update(UUID index, T obj);

    T read(UUID index);

    void delete(UUID index);
}
