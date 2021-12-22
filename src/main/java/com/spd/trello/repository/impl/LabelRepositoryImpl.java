package com.spd.trello.repository.impl;

import com.spd.trello.domain.Label;
import com.spd.trello.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LabelRepositoryImpl implements Repository<Label> {

    @Override
    public Label create(Label obj) {
        Label addedLabel = new Label();
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("INSERT INTO labels(id, created_date, color_name) VALUES(?, ?, ?);")) {
            statement.setObject(1, obj.getId());
            statement.setObject(2, obj.getCreatedDate());
            statement.setString(3, obj.getColorName());
            statement.executeUpdate();
            addedLabel = read(obj.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addedLabel;
    }

    @Override
    public Label update(UUID index, Label obj) {
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("UPDATE labels " +
                        "SET updated_date = ?," +
                        "color_name = ? " +
                        "WHERE id = ?")) {
        statement.setObject(1, obj.getUpdatedDate());
        statement.setString(2, obj.getColorName());
        statement.setObject(3, index);
        statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public Label read(UUID index) {
        Label label = new Label();
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("SELECT * FROM labels WHERE id = ?")) {
            statement.setObject(1, index);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            label.setId((UUID) resultSet.getObject("id"));
            label.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
            label.setColorName(resultSet.getString("color_name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }

    @Override
    public void delete(UUID index) {
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("DELETE FROM labels WHERE id = ?")) {
            statement.setObject(1, index);
            if (statement.executeUpdate() == 1)
                System.out.println("delete complete");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
