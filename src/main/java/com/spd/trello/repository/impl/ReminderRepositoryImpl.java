package com.spd.trello.repository.impl;

import com.spd.trello.domain.Card;
import com.spd.trello.domain.Reminder;
import com.spd.trello.repository.Repository;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class ReminderRepositoryImpl implements Repository<Reminder> {

    @Override
    public Reminder create(Reminder obj) {
        try (Connection connection = config.getConnection(); PreparedStatement statement = connection
                .prepareStatement("insert into reminders(id, created_by, created_date, starts, ends, alive, card_id) values " +
                        "(?,?,?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setObject(4, obj.getStart());
            statement.setObject(5, obj.getEnd());
            statement.setBoolean(6, obj.getAlive());
            statement.setObject(7, obj.getCard().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findById(obj.getId());
    }

    @Override
    public Reminder update(UUID index, Reminder obj) {
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "update reminders set updated_by=?, updated_date=?, starts=?, ends=?, remind_on=?, alive=? where id=?")) {
            statement.setString(1, obj.getUpdatedBy());
            statement.setTimestamp(2, Timestamp.valueOf(obj.getUpdatedDate()));
            statement.setTimestamp(3, Timestamp.valueOf(obj.getStart()));
            statement.setTimestamp(4, Timestamp.valueOf(obj.getEnd()));
            statement.setTimestamp(5, Timestamp.valueOf(obj.getRemindOn()));
            statement.setBoolean(6, obj.getAlive());
            statement.setObject(7, index);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findById(index);
    }

    @Override
    public Reminder findById(UUID index) {
        Reminder reminder = new Reminder();
        try (Connection connection = config.getConnection(); PreparedStatement statement = connection
                .prepareStatement("select * from reminders where id=?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                reminder = buildReminder(resultSet);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reminder;
    }

    Reminder buildReminder(ResultSet resultSet) throws SQLException {
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
    }

    @Override
    public boolean delete(UUID index) {
        return new Helper().delete(index, "DELETE FROM reminders WHERE id = ?");
    }
}