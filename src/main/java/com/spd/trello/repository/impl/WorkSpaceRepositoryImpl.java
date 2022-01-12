package com.spd.trello.repository.impl;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.Member;
import com.spd.trello.domain.WorkSpace;
import com.spd.trello.domain.WorkSpaceVisibility;
import com.spd.trello.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkSpaceRepositoryImpl implements Repository<WorkSpace> {

    private final MemberRepositoryImpl memberRepository;
    private final BoardRepositoryImpl boardRepository;

    public WorkSpaceRepositoryImpl() {
        memberRepository = new MemberRepositoryImpl();
        boardRepository = new BoardRepositoryImpl();
    }

    @Override
    public WorkSpace create(WorkSpace obj) {
        try (Connection connection = config.getConnection(); PreparedStatement statement =
                connection.prepareStatement("INSERT INTO workspaces(id, created_by, created_date, name, description, visibility) " +
                        "VALUES(?,?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getName());
            statement.setString(5, obj.getDescription());
            statement.setString(6, String.valueOf(obj.getVisibility()));
            statement.executeUpdate();
            addMemberRelations(obj, connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findById(obj.getId());
    }

    private void addMemberRelations(WorkSpace workSpace, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO space_member(space_id, member_id) VALUES (?,?)")) {

            for (Member member : workSpace.getWorkspaceMembers()) {
                statement.setObject(1, workSpace.getId());
                statement.setObject(2, member.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public WorkSpace update(UUID index, WorkSpace obj) {
        try (Connection connection = config.getConnection(); PreparedStatement statement =
                connection.prepareStatement("UPDATE workspaces SET updated_by=?, updated_date=?, name=?, description=?, visibility=? where id=?")) {
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getName());
            statement.setString(4, obj.getDescription());
            statement.setString(5, String.valueOf(obj.getVisibility()));
            statement.setObject(6, index);
            statement.executeUpdate();
            obj.setWorkspaceMembers(checkNewRelations(obj));
            if (!obj.getWorkspaceMembers().isEmpty())
                addMemberRelations(obj, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findById(index);
    }

    public List<Member> checkNewRelations(WorkSpace workSpace) throws SQLException {
        List<Member> members = workSpace.getWorkspaceMembers();
        List<Member> removedMembers = getMembersForWorkSpace(workSpace.getId());
        members.removeAll(removedMembers);
        return members;
    }

    @Override
    public WorkSpace findById(UUID index) {
        WorkSpace foundSpace = null;
        try (Connection connection = config.getConnection(); PreparedStatement statement = connection
                .prepareStatement("select * from workspaces where id = ?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                foundSpace = buildWorkSpace(resultSet);
                foundSpace.setWorkspaceMembers(getMembersForWorkSpace(index));
                foundSpace.setBoardList(getBoardsForWorkSpace(index));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundSpace;
    }

    WorkSpace buildWorkSpace(ResultSet resultSet) throws SQLException {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setId(UUID.fromString(resultSet.getString("id")));
        workSpace.setCreatedBy(resultSet.getString("created_by"));
        workSpace.setUpdatedBy(resultSet.getString("updated_by"));
        workSpace.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        workSpace.setUpdatedDate(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        workSpace.setVisibility(WorkSpaceVisibility.valueOf(resultSet.getString("visibility")));
        workSpace.setName(resultSet.getString("name"));
        workSpace.setDescription(resultSet.getString("description"));
        return workSpace;
    }

    public List<Member> getMembersForWorkSpace(UUID spaceId) throws SQLException {
        List<Member> members = new ArrayList<>();
        try (Connection connection = config.getConnection(); PreparedStatement statement = connection
                .prepareStatement("select m.id as id, m.created_by as created_by, m.updated_by as updated_by, m.updated_date as updated_date, m.created_date as created_date, m.role as role, m.user_id as user_id " +
                        "from workspaces w join space_member sm on w.id = sm.space_id join members m on m.id=sm.member_id where w.id = ?")) {
            statement.setObject(1, spaceId);

            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    members.add(memberRepository.buildMember(resultSet));
            }
        }
        return members;
    }

    private List<Board> getBoardsForWorkSpace(UUID spaceId) throws SQLException {
        List<Board> boards = new ArrayList<>();
        try (Connection connection = config.getConnection(); PreparedStatement statement = connection
                .prepareStatement("select * from boards where workspace_id = ?")) {
            statement.setObject(1, spaceId);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    boards.add(boardRepository.buildBoard(resultSet));
            }
        }
        return boards;
    }

    @Override
    public boolean delete(UUID index) {
        return new Helper().delete(index, "DELETE FROM workspaces WHERE id = ?");

    }

    @Override
    public List<WorkSpace> getObjects() {
        List<WorkSpace> workSpaces = new ArrayList<>();
        try (Connection connection = config.getConnection(); PreparedStatement statement = connection
                .prepareStatement("select * FROM workspaces")) {
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    workSpaces.add(buildWorkSpace(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workSpaces;
    }
}
