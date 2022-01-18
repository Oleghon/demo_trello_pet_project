package com.spd.trello.repository.impl;

import com.spd.trello.config.JdbcConfig;
import com.spd.trello.domain.Member;
import com.spd.trello.domain.Role;
import com.spd.trello.domain.User;
import com.spd.trello.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemberRepositoryImpl implements Repository<Member> {
    @Override
    public Member create(Member obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO members(id, created_by, created_date, role, user_id) " +
                            "VALUES(?,?,?,?,?)");
            statement.setObject(1, obj.getId());
            statement.setString(2, obj.getCreatedBy());
            statement.setObject(3, obj.getCreatedDate());
            statement.setString(4, String.valueOf(obj.getRole()));
            statement.setObject(5, obj.getUser().getId());
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public Member update(UUID index, Member obj) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE members SET role = ?, updated_by = ?, updated_date = ? WHERE id = ?");
            statement.setString(1, String.valueOf(obj.getRole()));
            statement.setString(2, obj.getUpdatedBy());
            statement.setObject(3, obj.getUpdatedDate());
            statement.setObject(4, index);
            statement.executeUpdate();
            return obj;
        });
    }

    @Override
    public Member findById(UUID index) {
        return JdbcConfig.execute((connection) -> {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM members WHERE id = ?");
            statement.setObject(1, index);
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next())
                    return buildMember(resultSet);
            }
            throw new RuntimeException("entity not found");
        });
    }

    Member buildMember(ResultSet resultSet) throws SQLException {
        Member member = new Member();
        User user = new User();
        member.setId(UUID.fromString(resultSet.getString("id")));
        member.setCreatedBy(resultSet.getString("created_by"));
        member.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
        member.setUpdatedBy(resultSet.getString("updated_by"));
        member.setUpdatedDate(Optional.ofNullable(resultSet.getTimestamp("updated_date"))
                .map(Timestamp::toLocalDateTime).orElse(null));
        member.setRole(Role.valueOf(resultSet.getString("role")));
        user.setId(UUID.fromString(resultSet.getString("user_id")));
        member.setUser(user);
        return member;
    }

    @Override
    public boolean delete(UUID index) {
        return new Helper().delete(index, "DELETE FROM members WHERE id = ?");
    }

    @Override
    public List<Member> getObjects() {
        return JdbcConfig.execute((connection) -> {
            List<Member> members = new ArrayList<>();
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM members");
            if (statement.execute()) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next())
                    members.add(buildMember(resultSet));
            }
            return members;
        });
    }
}
