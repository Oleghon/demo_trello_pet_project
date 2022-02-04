package com.spd.trello.repository.impl;

import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.resources.User;
import com.spd.trello.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MemberRepositoryImpl implements Repository<Member> {

    private final JdbcTemplate jdbcTemplate;
    final RowMapper<Member> memberMapper = (ResultSet resultSet, int rowNum) -> {
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
    };

    @Autowired
    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member create(Member obj) {
        jdbcTemplate.update("INSERT INTO members(id, created_by, created_date, role, user_id) VALUES(?,?,?,?,?)",
                obj.getId(), obj.getCreatedBy(), obj.getCreatedDate(), String.valueOf(obj.getRole()), obj.getUser().getId());
        return obj;
    }

    @Override
    public Member update(UUID index, Member obj) {
        jdbcTemplate.update("UPDATE members SET role = ?, updated_by = ?, updated_date = ? WHERE id = ?",
                String.valueOf(obj.getRole()), obj.getUpdatedBy(), obj.getUpdatedDate(), index);
        return obj;
    }

    @Override
    public Member findById(UUID index) {
        return jdbcTemplate.queryForObject("SELECT * FROM members WHERE id = ?", memberMapper, index);
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

    List<Member> getMembers(String sql, UUID id) {
        return jdbcTemplate.query(sql, memberMapper, id);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("DELETE FROM members WHERE id = ?", index) == 1;
    }

    @Override
    public List<Member> getObjects() {
        return jdbcTemplate.query("Select * from members", memberMapper);
    }
}
