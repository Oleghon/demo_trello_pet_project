package com.spd.trello.repository;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Repository<T extends Domain> {

    JdbcConfig config = new JdbcConfig();

    T create(T obj);

    T update(UUID index, T obj);

    T findById(UUID index);

    boolean delete(UUID index);

    default List<T> getObjects() {
        return new ArrayList<>();
    }

    class Helper {
        public boolean delete(UUID id, String SQL) {
            try (Connection connection = config.getConnection();
                 PreparedStatement statement = connection
                         .prepareStatement(SQL)) {
                statement.setObject(1, id);
                int i = statement.executeUpdate();
                if (i == 1)
                    return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
