package com.spd.trello.repository.impl;

import com.spd.trello.domain.resources.Label;
import com.spd.trello.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

@Component
public class LabelRepositoryImpl implements Repository<Label> {

    private final JdbcTemplate jdbcTemplate;
    RowMapper<Label> labelMapper = (ResultSet resultSet, int rowNum) -> {
        Label label = new Label();
        label.setId((UUID) resultSet.getObject("id"));
        label.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        label.setColorName(resultSet.getString("color_name"));
        return label;
    };

    @Autowired
    public LabelRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Label create(Label obj) {
        jdbcTemplate.update("INSERT INTO labels(id, created_date, color_name) VALUES(?, ?, ?)",
                obj.getId(),
                obj.getCreatedDate(),
                obj.getColorName());
        return obj;
    }

    @Override
    public Label update(UUID index, Label obj) {
        jdbcTemplate.update("UPDATE labels SET updated_date = ?, color_name = ? WHERE id = ?",
                obj.getUpdatedDate(),
                obj.getColorName(),
                index);
        return obj;
    }

    @Override
    public Label findById(UUID index) {
        return jdbcTemplate.queryForObject("SELECT * FROM labels WHERE id = ?", labelMapper, index);
    }

    @Override
    public List<Label> getObjects() {
        return jdbcTemplate.query("SELECT * FROM labels", labelMapper);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("DELETE FROM labels WHERE id = ?", index) == 1;
    }
}
