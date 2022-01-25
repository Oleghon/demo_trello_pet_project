package com.spd.trello.repository;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Resource;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Repository<T extends Resource> {

    T create(T obj);

    T update(UUID index, T obj);

    T findById(UUID index);

    boolean delete(UUID index);

    default List<T> getObjects() {
        return new ArrayList<>();
    }

}
