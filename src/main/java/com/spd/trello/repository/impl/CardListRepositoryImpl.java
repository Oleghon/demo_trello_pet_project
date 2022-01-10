package com.spd.trello.repository.impl;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.Card;
import com.spd.trello.domain.CardList;
import com.spd.trello.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CardListRepositoryImpl implements Repository<CardList> {

    private CardRepositoryImpl cardRepository;

    public CardListRepositoryImpl() {
        this.cardRepository = new CardRepositoryImpl();
    }

    @Override
    public CardList create(CardList obj) {
        try (Connection connection = config.getConnection(); PreparedStatement statement =
                connection.prepareStatement("INSERT INTO cardlists(id, created_by, created_date, name, archived, board_id) " +
                        "VALUES(?,?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getName());
            statement.setBoolean(5, obj.getArchived());
            statement.setObject(6, obj.getBoard().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CardList update(UUID index, CardList obj) {
        try (Connection connection = config.getConnection(); PreparedStatement statement =
                connection.prepareStatement("UPDATE cardlists SET updated_by=?, updated_date=?, name=?, archived=? where id=?")) {
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getName());
            statement.setBoolean(4, obj.getArchived());
            statement.setObject(5, index);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CardList findById(UUID index) {
        CardList cardList = null;
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("select * from cardlists where id = ?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                cardList = buildCardList(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardList;
    }

    private List<Card> getCardsForCardList(UUID id) throws SQLException {
        List<Card> cards = new ArrayList<>();
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("select id from cards where cardlist_id = ?")) {
            statement.setObject(1, id);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next())
                    cards.add(cardRepository.buildCard(resultSet));
            }
        }
        return cards;
    }

    CardList buildCardList(ResultSet resultSet) throws SQLException {
        CardList cardList = new CardList();
        Board board = new Board();
        cardList.setId(UUID.fromString(resultSet.getString("id")));
        cardList.setCreatedBy(resultSet.getString("created_by"));
        cardList.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        cardList.setUpdatedBy(Optional.ofNullable(resultSet.getString("updated_by"))
                .map(String::new).orElse(""));
        board.setId(UUID.fromString(resultSet.getString("board_id")));
        cardList.setBoard(board);
        cardList.setName(resultSet.getString("name"));
        cardList.setArchived(resultSet.getBoolean("archived"));
        return cardList;
    }

    @Override
    public boolean delete(UUID index) {
        boolean flag = false;
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM cardlists WHERE id = ?")) {
            statement.setObject(1, index);
            if (statement.executeUpdate() == 1)
                flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<CardList> getObjects() {
        List<CardList> cardLists = new ArrayList<>();
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("select * FROM cardlists")) {
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    cardLists.add(buildCardList(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardLists;
    }

}
