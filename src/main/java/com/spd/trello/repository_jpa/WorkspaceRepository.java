package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.WorkSpace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends CommonRepository<WorkSpace>{
    @Query(nativeQuery = true,
            value = "select * from workspaces as w inner join space_member wm on w.id = wm.space_id" +
            " where wm.member_id = :id UNION select * from workspaces as w" +
            " left join space_member wm on w.id = wm.space_id where w.visibility = 'PUBLIC'")
   List<WorkSpace> findAllByMemberId(UUID id);
}