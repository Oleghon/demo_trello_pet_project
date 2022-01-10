package com.spd.trello.repository.impl;

import com.spd.trello.domain.Card;
import com.spd.trello.domain.Comment;
import com.spd.trello.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CommentRepositoryImpl implements Repository<Comment> {
    @Override
    public Comment create(Comment obj) {
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("insert into comments(id, created_by, created_date, text, card_id)" +
                             "values (?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getText());
            statement.setObject(5, obj.getCard().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public Comment update(UUID index, Comment obj) {
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("update comments set updated_by=?, updated_date=?, text where id=?")) {
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getText());
            statement.setObject(4, index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public Comment findById(UUID index) {
        Comment foundComment = null;
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("select * from comments where id=?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                foundComment = buildComment(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundComment;
    }

    Comment buildComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        Card fk = new Card();
        comment.setCreatedBy(resultSet.getString("created_by"));
        comment.setText(resultSet.getString("text"));
        comment.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        fk.setId(UUID.fromString(resultSet.getString("card_id")));
        comment.setCard(fk);
        return buildComment(resultSet);
    }

    @Override
    public boolean delete(UUID index) {
        boolean flag = false;
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("delete * from comments where id=?")) {
            statement.setObject(1, index);
            if (statement.executeUpdate() == 1)
                flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
