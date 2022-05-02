package com.spd.trello.service;

import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.repository_jpa.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService extends AbstractService<Member, MemberRepository> {
    public MemberService(MemberRepository repository) {
        super(repository);
    }

    public Member createDefaultMember(UUID userId) {
        return create(userId, Role.GUEST);
    }

    private Member create(UUID userId, Role role) {
        Member member = new Member();
        member.setUserId(userId);
        member.setRole(role);
        return super.create(member);
    }

    public Member findByUserIdAndRole(UUID id, Role role) {
        return repository.findByUserIdAndRole(id, role)
                .orElseGet(() -> create(id, role));
    }
}
