package com.spd.trello.repository.impl;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Card;
import com.spd.trello.domain.CheckList;
import com.spd.trello.domain.CheckableItem;
import com.spd.trello.repository.Repository;
import com.spd.trello.repository.impl.helper.ItemsHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CheckListRepositoryImpl implements Repository<CheckList> {

    @Override
    public CheckList create(CheckList obj) {
       return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("insert into checklists(id, created_by, created_date, name, card_id)" +
                            "values (?,?,?,?,?)");
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getName());
            statement.setObject(5, obj.getCard().getId());
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public CheckList update(UUID index, CheckList obj) {
       return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("update checklists set updated_by=?, updated_date=?, name=? where id=?");
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getName());
            statement.setObject(4, index);
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public CheckList findById(UUID index) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("select * from checklists where id = ?");
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                CheckList checkList = buildCheckList(resultSet);
                checkList.setItems(getItems(checkList.getId(), connection));
                return checkList;
            }
            throw new RuntimeException();
        });
    }

    private List<CheckableItem> getItems(UUID id, Connection connection) throws SQLException {
        List<CheckableItem> checkableItems = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("select * from items where checklist_id=?")) {
            statement.setObject(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next())
                    checkableItems.add(ItemsHelper.buildItem(resultSet));
            }
        }
        return checkableItems;
    }

    CheckList buildCheckList(ResultSet resultSet) throws SQLException {
        CheckList checkList = new CheckList();
        Card fk = new Card();
        checkList.setId(UUID.fromString(resultSet.getString("id")));
        checkList.setCreatedBy(resultSet.getString("created_by"));
        checkList.setUpdatedBy(resultSet.getString("updated_by"));
        checkList.setName(resultSet.getString("name"));
        checkList.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        checkList.setUpdatedDate(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        fk.setId(UUID.fromString(resultSet.getString("card_id")));
        checkList.setCard(fk);
        return checkList;
    }

    @Override
    public boolean delete(UUID index) {
        return new Repository.Helper().delete(index, "delete from checklists where id=?");
    }
}
