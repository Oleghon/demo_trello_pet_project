package com.spd.trello.repository.impl;

import com.spd.trello.domain.Card;
import com.spd.trello.domain.Reminder;
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
public class ReminderRepositoryImpl implements Repository<Reminder> {

    private final JdbcTemplate jdbcTemplate;
    public RowMapper<Reminder> reminderMapper = (ResultSet resultSet, int rowNum) -> {
        Reminder reminder = new Reminder();
        Card card = new Card();
        reminder.setId(UUID.fromString(resultSet.getString("id")));
        reminder.setCreatedBy(resultSet.getString("created_by"));
        reminder.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        reminder.setRemindOn(Optional.ofNullable(resultSet.getTimestamp("remind_on"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        reminder.setStart(Optional.ofNullable(resultSet.getTimestamp("starts"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        reminder.setEnd(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        reminder.setAlive(resultSet.getBoolean("alive"));
        card.setId(UUID.fromString(resultSet.getString("card_id")));
        reminder.setCard(card);
        return reminder;
    };

    @Autowired
    public ReminderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reminder create(Reminder obj) {
        jdbcTemplate.update("insert into reminders(id, created_by, created_date, starts, ends, alive, card_id) values " +
                        "(?,?,?,?,?,?,?)",
                obj.getId(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getStart(),
                obj.getEnd(),
                obj.getAlive(),
                obj.getCard().getId());
        return obj;
    }

    @Override
    public Reminder update(UUID index, Reminder obj) {
        jdbcTemplate.update("update reminders set updated_by=?, updated_date=?, starts=?, ends=?, remind_on=?, alive=? where id=?",
                obj.getUpdatedBy(),
                Timestamp.valueOf(obj.getUpdatedDate()),
                Timestamp.valueOf(obj.getStart()),
                Timestamp.valueOf(obj.getEnd()),
                Timestamp.valueOf(obj.getRemindOn()),
                obj.getAlive(),
                index);
        return obj;
    }

    @Override
    public Reminder findById(UUID index) {
        return jdbcTemplate.queryForObject("select * from reminders where id=?", reminderMapper, index);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("DELETE FROM reminders WHERE id = ?", index) == 1;
    }
}