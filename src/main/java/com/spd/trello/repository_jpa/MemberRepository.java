package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends CommonRepository<Member>{
    @Query(nativeQuery = true, value = "select m.id, m.created_by, m.created_date, m.updated_by, m.updated_date, m.user_id, m.role " +
            "from members m left join space_member sm on m.id = sm.member_id " +
            "where sm.space_id =?2 " +
            "and m.user_id=?1")
    List<Member> findByUserIdAndWorkspacesExists(UUID userId, UUID workspaceId);
}
