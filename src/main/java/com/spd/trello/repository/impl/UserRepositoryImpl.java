package com.spd.trello.repository.impl;

import com.spd.trello.domain.User;
import com.spd.trello.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryImpl implements Repository<User> {

    private final RowMapper<User> userMapper = (ResultSet set, int rowNum) -> {
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
    };
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User obj) {
        jdbcTemplate.update("INSERT INTO users(id, created_by, created_date, first_name, last_name, email) " +
                "VALUES(?,?,?,?,?,?);", obj.getId(), obj.getCreatedBy(), obj.getCreatedDate(), obj.getFirstName(), obj.getLastName(), obj.getEmail());
        return obj;
    }

    @Override
    public User update(UUID index, User obj) {
        jdbcTemplate.update("UPDATE users SET first_name = ?, last_name = ?, email = ?, updated_by = ?, updated_date = ? WHERE id = ?",
                obj.getFirstName(), obj.getLastName(), obj.getEmail(), obj.getUpdatedBy(), obj.getUpdatedDate(), index);
        return obj;
    }

    @Override
    public User findById(UUID index) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?", userMapper, index);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", index) == 1;
    }

    @Override
    public List<User> getObjects() {
        return jdbcTemplate.query("SELECT * FROM users", userMapper);
    }
}
