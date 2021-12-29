package com.spd.trello.repository.impl;

import com.spd.trello.domain.Member;
import com.spd.trello.domain.WorkSpace;
import com.spd.trello.domain.WorkSpaceVisibility;
import com.spd.trello.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkSpaceRepositoryImpl implements Repository<WorkSpace> {

    private final MemberRepositoryImpl memberRepository;

    public WorkSpaceRepositoryImpl() {
        memberRepository = new MemberRepositoryImpl();
    }

    @Override
    public WorkSpace create(WorkSpace obj) {
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("INSERT INTO workspaces(id, created_by, created_date, name, description, visibility) " +
                        "VALUES(?,?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, obj.getName());
            statement.setString(5, obj.getDescription());
            statement.setString(6, String.valueOf(obj.getVisibility()));
            statement.executeUpdate();
            addMemberRelations(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void addMemberRelations(WorkSpace workSpace) throws SQLException {
        try (PreparedStatement statement = config.getConnection()
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
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("UPDATE workspaces SET updated_by=?, updated_date=?, name=?, description=?, visibility=? where id=?")) {
            statement.setString(1, obj.getUpdatedBy());
            statement.setObject(2, obj.getUpdatedDate());
            statement.setString(3, obj.getName());
            statement.setString(4, obj.getDescription());
            statement.setString(5, String.valueOf(obj.getVisibility()));
            statement.setObject(6, index);
            statement.executeUpdate();
//            obj.getWorkspaceMembers().removeAll(getUsersForWorkSpace(index));
//            if (!obj.getWorkspaceMembers().isEmpty())
//                addMemberRelations(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public WorkSpace findById(UUID index) {
        WorkSpace foundSpace = null;
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("select * from workspaces where id = ?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                foundSpace = buildWorkSpace(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundSpace;
    }

    private WorkSpace buildWorkSpace(ResultSet resultSet) throws SQLException {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setId(UUID.fromString(resultSet.getString("id")));
        workSpace.setCreatedBy(resultSet.getString("created_by"));
        workSpace.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        workSpace.setVisibility(WorkSpaceVisibility.valueOf(resultSet.getString("visibility")));
        workSpace.setName(resultSet.getString("name"));
        workSpace.setDescription(resultSet.getString("description"));
        workSpace.setWorkspaceMembers(getUsersForWorkSpace(workSpace.getId()));
//        workSpace.setBoardList();
        return workSpace;
    }

    private List<Member> getUsersForWorkSpace(UUID spaceId) throws SQLException {
        List<Member> members = new ArrayList<>();
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("select m.id as id, m.created_by as created_by, m.updated_by as updated_by, m.created_date as created_date, m.role as role, m.user_id as user_id " +
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

    @Override
    public void delete(UUID index) {
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("DELETE FROM workspaces WHERE id = ?")) {
            deleteRelations(index);
            statement.setObject(1, index);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRelations(UUID index) throws SQLException {
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("DELETE FROM space_member WHERE space_id = ?")) {
            statement.setObject(1, index);
            statement.executeUpdate();
        }
    }

    @Override
    public List<WorkSpace> getObjects() {
        List<WorkSpace> workSpaces = new ArrayList<>();
        try (PreparedStatement statement = config.getConnection()
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
