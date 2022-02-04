package com.spd.trello.repository.impl;

import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.Comment;
import com.spd.trello.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
public class CommentRepositoryImpl implements Repository<Comment> {

    private final JdbcTemplate jdbcTemplate;
    RowMapper<Comment> commentMapper = (ResultSet resultSet, int rowNum) -> {
        Comment comment = new Comment();
        Card fk = new Card();
        comment.setId(UUID.fromString(resultSet.getString("id")));
        comment.setCreatedBy(resultSet.getString("created_by"));
        comment.setUpdatedBy(resultSet.getString("updated_by"));
        comment.setText(resultSet.getString("text"));
        comment.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        comment.setUpdatedDate(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        fk.setId(UUID.fromString(resultSet.getString("card_id")));
        comment.setCard(fk);
        return comment;
    };

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment create(Comment obj) {
        jdbcTemplate.update("insert into comments(id, created_by, created_date, text, card_id)" +
                        "values (?,?,?,?,?)", obj.getId(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getText(),
                obj.getCard().getId());
        return obj;
    }

    @Override
    public Comment update(UUID index, Comment obj) {
        jdbcTemplate.update("update comments set updated_by=?, updated_date=?, text=? where id=?",
                obj.getUpdatedBy(),
                obj.getUpdatedDate(),
                obj.getText(),
                index);
        return obj;
    }

    @Override
    public Comment findById(UUID index) {
        return jdbcTemplate.queryForObject("select * from comments where id = ?", commentMapper, index);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("delete from comments where id=?", index) == 1;
    }
}
