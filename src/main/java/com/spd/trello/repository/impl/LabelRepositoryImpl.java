package com.spd.trello.repository.impl;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Label;
import com.spd.trello.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LabelRepositoryImpl implements Repository<Label> {

    @Override
    public Label create(Label obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO labels(id, created_date, color_name) VALUES(?, ?, ?);");
            statement.setObject(1, obj.getId());
            statement.setObject(2, obj.getCreatedDate());
            statement.setString(3, obj.getColorName());
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public Label update(UUID index, Label obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE labels SET updated_date = ?, color_name = ? WHERE id = ?");
            statement.setObject(1, obj.getUpdatedDate());
            statement.setString(2, obj.getColorName());
            statement.setObject(3, index);
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public Label findById(UUID index) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM labels WHERE id = ?");
            statement.setObject(1, index);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return buildLabel(resultSet);
            throw new RuntimeException();
        });
    }

    Label buildLabel(ResultSet resultSet) throws SQLException {
        Label label = new Label();
        label.setId((UUID) resultSet.getObject("id"));
        label.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        label.setColorName(resultSet.getString("color_name"));
        return label;
    }

    @Override
    public List<Label> getObjects() {
        return JdbcConfig.execute((connection) -> {
            List<Label> labels = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM labels");
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    labels.add(buildLabel(resultSet));
            }
            return labels;
        });
    }

    @Override
    public boolean delete(UUID index) {
        return new Repository.Helper().delete(index, "DELETE FROM labels WHERE id = ?");
    }
}
