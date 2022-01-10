package com.spd.trello.repository.impl;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.BoardVisibility;
import com.spd.trello.domain.CardList;
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

public class BoardRepositoryImpl implements Repository<Board> {

    private MemberRepositoryImpl memberRepository;
    private CardListRepositoryImpl cardListRepository;

    public BoardRepositoryImpl() {
        memberRepository = new MemberRepositoryImpl();
        cardListRepository = new CardListRepositoryImpl();
    }

    @Override
    public Board create(Board obj) {
        try (Connection connection = config.getConnection(); PreparedStatement statement =
                connection.prepareStatement("INSERT INTO boards(id, created_by, created_date, name, description, archived, visibility, workspace_id) " +
                        "VALUES(?,?,?,?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getName());
            statement.setString(5, obj.getDescription());
            statement.setBoolean(6, obj.getArchived());
            statement.setString(7, String.valueOf(obj.getVisibility()));
            statement.setObject(8, obj.getWorkSpace().getId());
            statement.executeUpdate();
            addMemberRelations(obj, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void addMemberRelations(Board obj, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO board_member(board_id, member_id) VALUES (?,?)")) {
            for (Member member : obj.getMembers()) {
                statement.setObject(1, obj.getId());
                statement.setObject(2, member.getId());
                statement.executeUpdate();
            }
        }
    }

    public Board buildBoard(ResultSet resultSet) throws SQLException {
        Board board = new Board();
        board.setId(UUID.fromString(resultSet.getString("id")));
        board.setCreatedBy(resultSet.getString("created_by"));
        board.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        board.setUpdatedBy(Optional.ofNullable(resultSet.getString("updated_by"))
                .map(String::new).orElse(""));
        board.setName(resultSet.getString("name"));
        board.setDescription(resultSet.getString("description"));
        board.setArchived(resultSet.getBoolean("archived"));
        board.setVisibility(BoardVisibility.valueOf(resultSet.getString("visibility")));
//        board.setCardLists();
        return board;
    }

    @Override
    public Board update(UUID index, Board obj) {
        try (Connection connection = config.getConnection(); PreparedStatement statement =
                connection.prepareStatement("UPDATE boards SET updated_by=?, updated_date=?, name=?, description=?, archived=?, visibility=? where id=?")) {
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getName());
            statement.setString(4, obj.getDescription());
            statement.setBoolean(5, obj.getArchived());
            statement.setString(6, String.valueOf(obj.getVisibility()));
            statement.setObject(7, index);
            statement.executeUpdate();
            obj.setMembers(checkNewRelations(obj));
            if (!obj.getMembers().isEmpty())
                addMemberRelations(obj, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findById(index);
    }

    public List<Member> checkNewRelations(Board board) throws SQLException {
        List<Member> members = board.getMembers();
        List<Member> removedMembers = getMembersForBoard(board.getId());
        members.removeAll(removedMembers);
        return members;
    }

    @Override
    public Board findById(UUID index) {
        Board foundBoard = null;
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select * from boards where id = ?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                foundBoard = buildBoard(resultSet);
                foundBoard.setMembers(getMembersForBoard(index));
                foundBoard.setCardLists(getCardLists(index, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundBoard;
    }

    private List<Member> getMembersForBoard(UUID index) throws SQLException {
        List<Member> members = new ArrayList<>();
        try (PreparedStatement statement = config.getConnection().prepareStatement(
                        "select m.id as id, m.created_by as created_by, m.updated_by as updated_by, " +
                        "m.created_date as created_date, m.role as role, m.user_id as user_id from boards b " +
                        "join board_member bm on b.id = bm.board_id " +
                        "join members m on bm.member_id = m.id where b.id = ?")) {
            statement.setObject(1, index);

            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    members.add(memberRepository.buildMember(resultSet));
            }
        }
        return members;
    }

    private List<CardList> getCardLists(UUID index, Connection connection) {
        List<CardList> cardLists = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from cardlists where board_id=?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    cardLists.add(cardListRepository.buildCardList(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardLists;
    }

    @Override
    public List<Board> getObjects() {
        List<Board> boards = new ArrayList<>();
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("select * FROM boards")) {
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    boards.add(buildBoard(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    @Override
    public boolean delete(UUID index) {
        boolean flag = false;
        try (Connection connection = config.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM boards WHERE id = ?")) {
            statement.setObject(1, index);
            if (statement.executeUpdate() == 1)
                flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

}
