package com.spd.trello.repository.impl;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.Member;
import com.spd.trello.domain.WorkSpace;
import com.spd.trello.domain.WorkSpaceVisibility;
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
public class WorkSpaceRepositoryImpl implements Repository<WorkSpace> {

    private final MemberRepositoryImpl memberRepository;
    private final BoardRepositoryImpl boardRepository;
    private final JdbcTemplate jdbcTemplate;
    RowMapper<WorkSpace> spaceMapper = (ResultSet resultSet, int rowNum) -> {
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
    };

    @Autowired
    public WorkSpaceRepositoryImpl(MemberRepositoryImpl memberRepository, BoardRepositoryImpl boardRepository, JdbcTemplate jdbcTemplate) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public WorkSpace create(WorkSpace obj) {
        jdbcTemplate.update("INSERT INTO workspaces(id, created_by, created_date, name, description, visibility) VALUES(?,?,?,?,?,?)",
                obj.getId(),
                obj.getCreatedBy(),
                obj.getCreatedDate(),
                obj.getName(),
                obj.getDescription(),
                String.valueOf(obj.getVisibility()));
        addMemberRelations(obj);
        return obj;
    }

    private void addMemberRelations(WorkSpace workSpace) {
        for (Member member : workSpace.getWorkspaceMembers()) {
            jdbcTemplate.update("INSERT INTO space_member(space_id, member_id) VALUES (?,?)",
                    workSpace.getId(),
                    member.getId());
        }
    }

    @Override
    public WorkSpace update(UUID index, WorkSpace obj) {
        jdbcTemplate.update("UPDATE workspaces SET updated_by=?, updated_date=?, name=?, description=?, visibility=? where id=?",
                obj.getUpdatedBy(),
                obj.getUpdatedDate(),
                obj.getName(),
                obj.getDescription(),
                String.valueOf(obj.getVisibility()),
                index);
        obj.setWorkspaceMembers(checkNewRelations(obj));
        if (!obj.getWorkspaceMembers().isEmpty())
            addMemberRelations(obj);
        return obj;
    }

    public List<Member> checkNewRelations(WorkSpace workSpace) {
        List<Member> members = workSpace.getWorkspaceMembers();
        List<Member> removedMembers = getMembersForWorkSpace(workSpace.getId());
        members.removeAll(removedMembers);
        return members;
    }

    @Override
    public WorkSpace findById(UUID index) {
        WorkSpace workSpace = jdbcTemplate.queryForObject("select * from workspaces where id = ?", spaceMapper, index);
        workSpace.setWorkspaceMembers(getMembersForWorkSpace(index));
        workSpace.setBoardList(getBoardsForWorkSpace(index));
        return workSpace;
    }

    public List<Member> getMembersForWorkSpace(UUID spaceId) {
        return memberRepository.getMembers("select m.id as id, m.created_by as created_by, m.updated_by as updated_by, m.updated_date as updated_date, m.created_date as created_date, m.role as role, m.user_id as user_id " +
                "from workspaces w join space_member sm on w.id = sm.space_id join members m on m.id=sm.member_id where w.id = ?", spaceId);
    }

    private List<Board> getBoardsForWorkSpace(UUID spaceId) {
        return jdbcTemplate.query("select * from boards where workspace_id = ?", boardRepository.boardMapper, spaceId);
    }

    @Override
    public boolean delete(UUID index) {
        return jdbcTemplate.update("DELETE FROM workspaces WHERE id = ?", index) == 1;
    }

    @Override
    public List<WorkSpace> getObjects() {
        return jdbcTemplate.query("select * FROM workspaces", spaceMapper);
    }
}
