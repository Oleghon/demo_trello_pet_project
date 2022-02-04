package com.spd.trello.repository.impl;

import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.CardList;
import com.spd.trello.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CardListRepositoryImpl implements Repository<CardList> {

    private CardRepositoryImpl cardRepository;
    private final JdbcTemplate jdbcTemplate;
    RowMapper<CardList> cardListMapper = (ResultSet resultSet, int rowNum) -> {
        CardList cardList = new CardList();
        Board board = new Board();
        cardList.setId(UUID.fromString(resultSet.getString("id")));
        cardList.setCreatedBy(resultSet.getString("created_by"));
        cardList.setUpdatedBy(resultSet.getString("updated_by"));
        cardList.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        cardList.setUpdatedDate(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        board.setId(UUID.fromString(resultSet.getString("board_id")));
        cardList.setBoard(board);
        cardList.setName(resultSet.getString("name"));
        cardList.setArchived(resultSet.getBoolean("archived"));
        return cardList;
    };

    @Autowired
    public CardListRepositoryImpl(CardRepositoryImpl cardRepository, JdbcTemplate jdbcTemplate) {
        this.cardRepository = cardRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CardList create(CardList obj) {
        jdbcTemplate.update("INSERT INTO cardlists(id, created_by, created_date, name, archived, board_id) VALUES(?,?,?,?,?,?)",
                obj.getId(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getName(),
                obj.getArchived(), obj.getBoard().getId());
        return obj;
    }

    @Override
    public CardList update(UUID index, CardList obj) {
        jdbcTemplate.update("UPDATE cardlists SET updated_by=?, updated_date=?, name=?, archived=? where id=?",
                obj.getUpdatedBy(),
                obj.getUpdatedDate(),
                obj.getName(),
                obj.getArchived(),
                index);
        return obj;
    }

    @Override
    public CardList findById(UUID index) {
        CardList cardList = jdbcTemplate.queryForObject("select * from cardlists where id = ?", cardListMapper, index);
        cardList.setCards(getCardsForCardList(index));
        return cardList;
    }

    private List<Card> getCardsForCardList(UUID id) {
        return jdbcTemplate.query("select * from cards where cardlist_id = ?", cardRepository.cardMapper, id);

    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("DELETE FROM cardlists WHERE id = ?", index) == 1;
    }

    @Override
    public List<CardList> getObjects() {
        return jdbcTemplate.query("select * FROM cardlists", cardListMapper);
    }
}
