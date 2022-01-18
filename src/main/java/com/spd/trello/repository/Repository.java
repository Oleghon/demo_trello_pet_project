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

    class Helper {
        public boolean delete(UUID id, String SQL) {
            return JdbcConfig.execute((connection) -> {
                PreparedStatement statement = connection
                        .prepareStatement(SQL);
                statement.setObject(1, id);
                int i = statement.executeUpdate();
                if (i == 1)
                    return true;
                return false;
            });
        }
    }
}
