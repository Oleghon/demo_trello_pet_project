package com.spd.trello.repository.impl;

import com.spd.trello.domain.Card;
import com.spd.trello.domain.CheckList;
import com.spd.trello.repository.Repository;
import com.spd.trello.repository.impl.helper.ItemsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Component
public class CheckListRepositoryImpl implements Repository<CheckList> {

    private final JdbcTemplate jdbcTemplate;
    private ItemsHelper itemsHelper;
    RowMapper<CheckList> checkListMapper = (ResultSet resultSet, int rowNum) -> {
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
    };

    @Autowired
    public CheckListRepositoryImpl(JdbcTemplate jdbcTemplate, ItemsHelper itemHelper) {
        this.jdbcTemplate = jdbcTemplate;
        this.itemsHelper = itemHelper;
    }

    @Override
    public CheckList create(CheckList obj) {
        jdbcTemplate.update("insert into checklists(id, created_by, created_date, name, card_id) values (?,?,?,?,?)",
                obj.getId(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getName(),
                obj.getCard().getId());
        return obj;
    }

    @Override
    public CheckList update(UUID index, CheckList obj) {
        jdbcTemplate.update("update checklists set updated_by=?, updated_date=?, name=? where id=?",
                obj.getUpdatedBy(),
                obj.getUpdatedDate(),
                obj.getName(),
                index);
        return obj;
    }

    @Override
    public CheckList findById(UUID index) {
        CheckList checkList = jdbcTemplate.queryForObject("select * from checklists where id = ?", checkListMapper, index);
        checkList.setItems(itemsHelper.getItems(checkList.getId()));
        return checkList;
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("delete from checklists where id=?", index) == 1;
    }
}
