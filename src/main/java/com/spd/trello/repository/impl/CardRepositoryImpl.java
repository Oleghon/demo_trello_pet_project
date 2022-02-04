package com.spd.trello.repository.impl;

import com.spd.trello.domain.resources.*;
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
public class CardRepositoryImpl implements Repository<Card> {

    private MemberRepositoryImpl memberRepository;
    private CommentRepositoryImpl commentRepository;
    private ReminderRepositoryImpl reminderRepository;
    private CheckListRepositoryImpl checkListRepository;
    private final JdbcTemplate jdbcTemplate;
    RowMapper<Card> cardMapper = (ResultSet resultSet, int rowNum) -> {
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
        return card;
    };

    @Autowired
    public CardRepositoryImpl(MemberRepositoryImpl memberRepository, CommentRepositoryImpl commentRepository,
                              ReminderRepositoryImpl reminderRepository, CheckListRepositoryImpl checkListRepository, JdbcTemplate jdbcTemplate) {
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.reminderRepository = reminderRepository;
        this.checkListRepository = checkListRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Card create(Card obj) {
        jdbcTemplate.update("insert into cards(id, created_by, created_date, name, description, archived, cardlist_id) values (?,?,?,?,?,?,?)",
                obj.getId(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getName(),
                obj.getDescription(),
                obj.getArchived(),
                obj.getCardList().getId());
        addMemberRelations(obj);
        return obj;
    }

    private void addMemberRelations(Card obj) {
        for (Member member : obj.getAssignedMembers()) {
            jdbcTemplate.update("INSERT INTO card_member(card_id, member_id) VALUES (?,?)",
                    obj.getId(), member.getId());
        }
    }

    @Override
    public Card update(UUID index, Card obj) {
        jdbcTemplate.update("update cards set updated_by=?, updated_date=?, name=?, description=?, archived=?, cardlist_id=? where id=?",
                obj.getUpdatedBy(),
                obj.getUpdatedDate(),
                obj.getName(),
                obj.getDescription(),
                obj.getArchived(),
                obj.getCardList().getId(),
                index);
        obj.setAssignedMembers(checkNewRelations(obj));
        if (!obj.getAssignedMembers().isEmpty())
            addMemberRelations(obj);
        return obj;
    }

    public List<Member> checkNewRelations(Card card) {
        List<Member> members = card.getAssignedMembers();
        List<Member> removedMembers = getMembersForCard(card.getId());
        members.removeAll(removedMembers);
        return members;
    }

    @Override
    public Card findById(UUID index) {
        Card foundCard = jdbcTemplate.queryForObject("select * from cards where id=?", cardMapper, index);
        foundCard.setComments(getCommentsForCard(index));
        foundCard.setAssignedMembers(getMembersForCard(index));
        //foundCard.setReminder(getReminder(index));
        foundCard.setCheckList(getCheckListsForCard(index));
        return foundCard;

    }

    private List<Member> getMembersForCard(UUID index) {
        return memberRepository.getMembers("select m.id as id, m.created_by as created_by, m.updated_by as updated_by, " +
                "m.created_date as created_date, m.updated_date as updated_date, m.role as role, m.user_id as user_id from cards c " +
                "join card_member cm on cm.card_id = c.id " +
                "join members m on cm.member_id = m.id where c.id = ?", index);
    }

    private List<CheckList> getCheckListsForCard(UUID uuid) {
        return jdbcTemplate.query("select * from checklists where card_id=?",
                checkListRepository.checkListMapper, uuid);
    }

    private List<Comment> getCommentsForCard(UUID uuid) {
        return jdbcTemplate.query("select * from comments where card_id=?",
                commentRepository.commentMapper, uuid);
    }

    private Reminder getReminder(UUID uuid) {
        return jdbcTemplate.queryForObject("select * from reminders where card_id=?",
                reminderRepository.reminderMapper, uuid);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("delete from cards where id=?", index) == 1;
    }

    @Override
    public List<Card> getObjects() {
        return jdbcTemplate.query("Select * from cards", cardMapper);
    }
}
