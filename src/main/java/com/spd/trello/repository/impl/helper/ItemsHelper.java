package com.spd.trello.repository.impl.helper;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.CheckList;
import com.spd.trello.domain.CheckableItem;
import com.spd.trello.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ItemsHelper {

    public CheckableItem create(CheckableItem obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("insert into items(id, name, checked, checklist_id)" +
                            "values (?,?,?,?)");
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getName());
            statement.setBoolean(3, obj.getCheck());
            statement.setObject(4, obj.getCheckList().getId());
            statement.executeUpdate();
            return obj;
        });
    }

    public static CheckableItem buildItem(ResultSet resultSet) throws SQLException {
        CheckableItem item = new CheckableItem();
        CheckList checkList = new CheckList();
        item.setId(UUID.fromString(resultSet.getString("id")));
        item.setName(resultSet.getString("name"));
        item.setCheck(resultSet.getBoolean("checked"));
        checkList.setId(UUID.fromString(resultSet.getString("checklist_id")));
        item.setCheckList(checkList);
        return item;
    }

    public boolean delete(UUID id) {
        return new Repository.Helper().delete(id, "delete from items where id =?");
    }

}
