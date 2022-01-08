package com.spd.trello.repository.impl;

import com.spd.trello.domain.Card;
import com.spd.trello.domain.Member;
import com.spd.trello.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CardRepositoryImpl implements Repository<Card> {

    private MemberRepositoryImpl memberRepository;

    public CardRepositoryImpl() {
        this.memberRepository = new MemberRepositoryImpl();
    }

    @Override
    public Card create(Card obj) {
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("insert into cards(id, created_by, created_date, name, description, archived, cardlist_id)" +
                             "values (?,?,?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getName());
            statement.setString(5, obj.getDescription());
            statement.setBoolean(6, obj.getArchived());
            statement.setObject(7, obj.getCardList().getId());
            statement.executeUpdate();
            addMemberRelations(obj, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void addMemberRelations(Card obj, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO card_member(card_id, member_id) VALUES (?,?)")) {
            for (Member member : obj.getAssignedMembers()) {
                statement.setObject(1, obj.getId());
                statement.setObject(2, member.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public Card update(UUID index, Card obj) {
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("update card set updated_by=?, updated_date=?, name=?, description=?, archived=?, cardlist_id=? where id=?")) {
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getName());
            statement.setString(4, obj.getDescription());
            statement.setBoolean(5, obj.getArchived());
            statement.setObject(6, obj.getCardList().getId());
            statement.setObject(7, index);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public Card findById(UUID index) {
        Card foundCard = null;
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("select * from cards where id=?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                foundCard = buildCard(resultSet);
                foundCard.setAssignedMembers(getMembersForCard(index));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundCard;
    }

    public Card buildCard(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        card.setId(UUID.fromString(resultSet.getString("id")));
        card.setCreatedBy(resultSet.getString("created_by"));
        card.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        card.setCreatedBy(Optional.ofNullable(resultSet.getString("updated_by"))
                .map(String::new).orElse(""));
        card.setName(resultSet.getString("name"));
        card.setDescription(resultSet.getString("description"));
        card.setArchived(resultSet.getBoolean("archived"));
        return card;
    }

    private List<Member> getMembersForCard(UUID index) throws SQLException {
        List<Member> members = new ArrayList<>();
        try (Connection connection = config.getConnection(); PreparedStatement statement = connection
                .prepareStatement("select m.id as id, m.created_by as created_by, m.updated_by as updated_by, " +
                        "m.created_date as created_date, m.role as role, m.user_id as user_id from cards c " +
                        "join card_member cm on cm.card_id = c.id " +
                        "join members m on cm.member_id = m.id where c.id = ?")) {
            statement.setObject(1, index);

            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    members.add(memberRepository.buildMember(resultSet));
            }
        }
        return members;
    }

    @Override
    public void delete(UUID index) {
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("delete from cards where id=?")) {
            statement.setObject(1, index);
            deleteRelations(index, connection);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRelations(UUID index, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement("DELETE FROM card_member WHERE card_id = ?")) {
            statement.setObject(1, index);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Card> getObjects() {
        List<Card> cards = new ArrayList<>();
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("select * from cards")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                cards.add(buildCard(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
}
