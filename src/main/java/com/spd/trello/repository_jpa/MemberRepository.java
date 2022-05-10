package com.spd.trello.repository_jpa;

import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.resources.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends CommonRepository<Member>{

    List<Member> findAllByUserId(UUID id);

    Optional<Member> findByUserIdAndRole(UUID id, Role role);
}
