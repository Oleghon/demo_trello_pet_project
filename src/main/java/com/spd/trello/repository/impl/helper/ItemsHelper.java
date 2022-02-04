package com.spd.trello.repository.impl.helper;

import com.spd.trello.domain.resources.CheckList;
import com.spd.trello.domain.items.CheckableItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

@Component
public class ItemsHelper {

    private final JdbcTemplate jdbcTemplate;
    private RowMapper<CheckableItem> itemMapper = (ResultSet resultSet, int rowNum) -> {
        CheckableItem item = new CheckableItem();
        CheckList checkList = new CheckList();
        item.setId(UUID.fromString(resultSet.getString("id")));
        item.setName(resultSet.getString("name"));
        item.setCheck(resultSet.getBoolean("checked"));
        checkList.setId(UUID.fromString(resultSet.getString("checklist_id")));
        item.setCheckList(checkList);
        return item;
    };

    @Autowired
    public ItemsHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CheckableItem create(CheckableItem obj) {
        jdbcTemplate.update("insert into items(id, name, checked, checklist_id)" +
                "values (?,?,?,?)", obj.getId(), obj.getName(), obj.getCheck(), obj.getCheckList().getId());
        return obj;
    }

    public List<CheckableItem> getItems(UUID id) {
        return jdbcTemplate.query("select * from items where checklist_id=?", itemMapper, id);
    }

    public boolean delete(UUID id) {
        return jdbcTemplate.update("delete from items where id =?", id) == 1;
    }

}
