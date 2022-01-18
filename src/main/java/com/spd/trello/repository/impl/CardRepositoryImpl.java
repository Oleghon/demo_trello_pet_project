package com.spd.trello.repository.impl;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.*;
import com.spd.trello.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CardRepositoryImpl implements Repository<Card> {

    private MemberRepositoryImpl memberRepository;
    private CommentRepositoryImpl commentRepository;
    private ReminderRepositoryImpl reminderRepository;
    private CheckListRepositoryImpl checkListRepository;

    public CardRepositoryImpl() {
        this.memberRepository = new MemberRepositoryImpl();
        this.commentRepository = new CommentRepositoryImpl();
        this.reminderRepository = new ReminderRepositoryImpl();
        this.checkListRepository = new CheckListRepositoryImpl();
    }

    @Override
    public Card create(Card obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("insert into cards(id, created_by, created_date, name, description, archived, cardlist_id)" +
                            "values (?,?,?,?,?,?,?)");
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getName());
            statement.setString(5, obj.getDescription());
            statement.setBoolean(6, obj.getArchived());
            statement.setObject(7, obj.getCardList().getId());
            statement.executeUpdate();
            addMemberRelations(obj, connection);
            return obj;
        });
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
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("update cards set updated_by=?, updated_date=?, name=?, description=?, archived=?, cardlist_id=? where id=?");
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getName());
            statement.setString(4, obj.getDescription());
            statement.setBoolean(5, obj.getArchived());
            statement.setObject(6, obj.getCardList().getId());
            statement.setObject(7, index);
            statement.executeUpdate();
            obj.setAssignedMembers(checkNewRelations(obj));
            if (!obj.getAssignedMembers().isEmpty()) {
                System.out.println(obj.getAssignedMembers());
                addMemberRelations(obj, connection);
            }
            return obj;
        });
    }

    public List<Member> checkNewRelations(Card card) throws SQLException {
        List<Member> members = card.getAssignedMembers();
        List<Member> removedMembers = getMembersForCard(card.getId());
        members.removeAll(removedMembers);
        return members;
    }

    @Override
    public Card findById(UUID index) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("select * from cards where id=?");
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                Card foundCard = buildCard(resultSet);
                foundCard.setComments(getCommentsForCard(index, connection));
                foundCard.setAssignedMembers(getMembersForCard(index));
                foundCard.setReminder(getReminder(index, connection));
                foundCard.setCheckList(getCheckListsForCard(index, connection));
                return foundCard;
            }
            throw new RuntimeException("Entity card not found by id: " + index);
        });
    }

    Card buildCard(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        CardList cardList = new CardList();
        card.setId(UUID.fromString(resultSet.getString("id")));
        card.setCreatedBy(resultSet.getString("created_by"));
        card.setUpdatedBy(resultSet.getString("updated_by"));
        card.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        card.setUpdatedDate(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        card.setName(resultSet.getString("name"));
        card.setDescription(resultSet.getString("description"));
        card.setArchived(resultSet.getBoolean("archived"));
        cardList.setId(UUID.fromString(resultSet.getString("cardlist_id")));
        card.setCardList(cardList);
//        card.setLabels();
        return card;
    }

    private List<Member> getMembersForCard(UUID index) {
        return JdbcConfig.execute((connection) -> {
            List<Member> members = new ArrayList<>();
            PreparedStatement statement = connection
                    .prepareStatement("select m.id as id, m.created_by as created_by, m.updated_by as updated_by, " +
                            "m.created_date as created_date, m.updated_date as updated_date, m.role as role, m.user_id as user_id from cards c " +
                            "join card_member cm on cm.card_id = c.id " +
                            "join members m on cm.member_id = m.id where c.id = ?");
            statement.setObject(1, index);

            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    members.add(memberRepository.buildMember(resultSet));
            }
            return members;
        });
    }

    private List<Comment> getCommentsForCard(UUID uuid, Connection connection) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("select * from comments where card_id=?")) {
            statement.setObject(1, uuid);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next())
                    comments.add(commentRepository.buildComment(resultSet));
            }
        }
        return comments;
    }

    private List<CheckList> getCheckListsForCard(UUID uuid, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement("select * from checklists where card_id=?")) {
            List<CheckList> checkLists = new ArrayList<>();
            statement.setObject(1, uuid);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next())
                    checkLists.add(checkListRepository.buildCheckList(resultSet));
            }
            return checkLists;
        }
    }

    private Reminder getReminder(UUID uuid, Connection connection) throws SQLException {
        Reminder reminder = new Reminder();
        try (PreparedStatement statement = connection
                .prepareStatement("select * from reminders where card_id=?")) {
            statement.setObject(1, uuid);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                    reminder = reminderRepository.buildReminder(resultSet);
            }
        }
        return reminder;
    }

    @Override
    public boolean delete(UUID index) {
        return new Repository.Helper().delete(index, "delete from cards where id=?");
    }

    @Override
    public List<Card> getObjects() {
        return JdbcConfig.execute((connection) -> {
            List<Card> cards = new ArrayList<>();
            PreparedStatement statement = connection
                    .prepareStatement("select * from cards");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                cards.add(buildCard(resultSet));
            return cards;
        });
    }
}
