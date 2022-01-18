package com.spd.trello.repository.impl;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.User;
import com.spd.trello.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements Repository<User> {
    @Override
    public User create(User obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO users(id, created_by, created_date, first_name, last_name, email) " +
                            "VALUES(?,?,?,?,?,?);");
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getFirstName());
            statement.setString(5, obj.getLastName());
            statement.setString(6, obj.getEmail());
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public User update(UUID index, User obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE users SET first_name = ?, last_name = ?, email = ?, updated_by = ?, updated_date = ? WHERE id = ?");
            statement.setString(1, obj.getFirstName());
            statement.setString(2, obj.getLastName());
            statement.setString(3, obj.getEmail());
            statement.setString(4, obj.getUpdatedBy());
            statement.setObject(5, obj.getUpdatedDate());
            statement.setObject(6, index);
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public User findById(UUID index) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return buildUser(resultSet);
            }
            throw new RuntimeException("entity not found");
        });
    }

    @Override
    public boolean delete(UUID index) {
        return new Helper().delete(index, "DELETE FROM users WHERE id = ?");
    }

    private User buildUser(ResultSet set) throws SQLException {
        User result = new User();
        result.setId(UUID.fromString(set.getString("id")));
        result.setCreatedBy(set.getString("created_by"));
        result.setCreatedDate(set.getTimestamp("created_date").toLocalDateTime());
        result.setUpdatedBy(set.getString("updated_by"));
        result.setUpdatedDate(Optional.ofNullable(set.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        result.setFirstName(set.getString("first_name"));
        result.setLastName(set.getString("last_name"));
        result.setEmail(set.getString("email"));
        return result;
    }

    @Override
    public List<User> getObjects() {
        return JdbcConfig.execute((connection) ->
        {
            List<User> users = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {
                if (statement.execute()) {
                    ResultSet resultSet = statement.getResultSet();
                    while (resultSet.next())
                        users.add(buildUser(resultSet));
                }
                return users;
            }
        });
    }
}
