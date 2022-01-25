package com.spd.trello.repository.impl;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.BoardVisibility;
import com.spd.trello.domain.CardList;
import com.spd.trello.domain.Member;
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
public class BoardRepositoryImpl implements Repository<Board> {

    private MemberRepositoryImpl memberRepository;
    private CardListRepositoryImpl cardListRepository;
    private final JdbcTemplate jdbcTemplate;
    RowMapper<Board> boardMapper = (ResultSet resultSet, int rowNum) -> {
        Board board = new Board();
        board.setId(UUID.fromString(resultSet.getString("id")));
        board.setCreatedBy(resultSet.getString("created_by"));
        board.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        board.setUpdatedDate(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        board.setUpdatedBy(resultSet.getString("updated_by"));
        board.setName(resultSet.getString("name"));
        board.setDescription(resultSet.getString("description"));
        board.setArchived(resultSet.getBoolean("archived"));
        board.setVisibility(BoardVisibility.valueOf(resultSet.getString("visibility")));
        return board;
    };

    @Autowired
    public BoardRepositoryImpl(MemberRepositoryImpl memberRepository, CardListRepositoryImpl cardListRepository, JdbcTemplate jdbcTemplate) {
        this.memberRepository = memberRepository;
        this.cardListRepository = cardListRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Board create(Board obj) {
        jdbcTemplate.update("INSERT INTO boards(id, created_by, created_date, name, description, archived, visibility, workspace_id) " +
                        "VALUES(?,?,?,?,?,?,?,?)",
                obj.getId(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getName(),
                obj.getDescription(), obj.getArchived(),
                String.valueOf(obj.getVisibility()),
                obj.getWorkSpace().getId());
        addMemberRelations(obj);
        return obj;
    }

    private void addMemberRelations(Board obj) {
        for (Member member : obj.getMembers()) {
            jdbcTemplate.update("INSERT INTO board_member(board_id, member_id) VALUES (?,?)",
                    obj.getId(), member.getId());
        }
    }

    @Override
    public Board update(UUID index, Board obj) {
        jdbcTemplate.update("UPDATE boards SET updated_by=?, updated_date=?, name=?, description=?, archived=?, visibility=? where id=?",
                obj.getUpdatedBy(),
                obj.getUpdatedDate(),
                obj.getName(),
                obj.getDescription(),
                obj.getArchived(),
                String.valueOf(obj.getVisibility()),
                index);
        obj.setMembers(checkNewRelations(obj));
        if (!obj.getMembers().isEmpty()) {
            addMemberRelations(obj);
        }
        return obj;
    }

    public List<Member> checkNewRelations(Board board) {
        List<Member> members = board.getMembers();
        List<Member> removedMembers = getMembersForBoard(board.getId());
        members.removeAll(removedMembers);
        return members;
    }

    @Override
    public Board findById(UUID index) {
        Board board = jdbcTemplate.queryForObject("select * from boards where id = ?", boardMapper, index);
        board.setMembers(getMembersForBoard(index));
        board.setCardLists(getCardLists(index));
        return board;
    }

    private List<Member> getMembersForBoard(UUID index) {
        return memberRepository.getMembers("select m.id as id, m.created_by as created_by, m.updated_by as updated_by, " +
                "m.created_date as created_date, m.updated_date as updated_date, m.role as role, m.user_id as user_id from boards b " +
                "join board_member bm on b.id = bm.board_id " +
                "join members m on bm.member_id = m.id where b.id = ?", index);
    }

    private List<CardList> getCardLists(UUID index) {
        return jdbcTemplate.query("select * from cardlists where board_id=?", cardListRepository.cardListMapper, index);
    }

    @Override
    public List<Board> getObjects() {
        return jdbcTemplate.query("select * FROM boards", boardMapper);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("DELETE FROM boards WHERE id = ?", index) == 1;
    }
}
