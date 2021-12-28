package com.spd.trello.repository.impl;

import com.spd.trello.domain.Member;
import com.spd.trello.domain.Role;
import com.spd.trello.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemberRepositoryImpl implements Repository<Member> {
    @Override
    public Member create(Member obj) {
        Member createdMember = null;
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("INSERT INTO members(id, created_by, created_date, role, user_id) " +
                        "VALUES(?,?,?,?,?)")) {
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, String.valueOf(obj.getRole()));
            statement.setObject(5, obj.getUser().getId());
            statement.executeUpdate();
            createdMember = obj;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return createdMember;
    }

    @Override
    public Member update(UUID index, Member obj) {
        Member updatedMember = null;
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("UPDATE members SET role = ?, updated_by = ?, updated_date = ? WHERE id = ?")) {
            statement.setString(1, String.valueOf(obj.getRole()));
            statement.setString(2, obj.getUpdatedBy());
            statement.setObject(3, obj.getUpdatedDate());
            statement.setObject(4, index);
            statement.executeUpdate();
            updatedMember = findById(index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedMember;
    }

    @Override
    public Member findById(UUID index) {
        Member foundMember = null;
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("SELECT * FROM members WHERE id = ?")) {
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                foundMember = buildMember(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundMember;
    }

    private Member buildMember(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        member.setId(UUID.fromString(resultSet.getString("id")));
        member.setCreatedBy(resultSet.getString("created_by"));
        member.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        member.setRole(Role.valueOf(resultSet.getString("role")));
        member.setUser(new UserRepositoryImpl().findById(UUID.fromString(resultSet.getString("user_id"))));
        return member;
    }


    @Override
    public void delete(UUID index) {
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("DELETE FROM members WHERE id = ?")) {
            statement.setObject(1, index);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Member> getObjects() {
        List<Member> members = new ArrayList<>();
        try (PreparedStatement statement = config.getConnection()
                .prepareStatement("SELECT * FROM members")) {
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    members.add(buildMember(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
}
