package com.spd.trello.service;

import com.spd.trello.domain.resources.Member;
import com.spd.trello.repository_jpa.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends AbstractService<Member, MemberRepository> {
    public MemberService(MemberRepository repository) {
        super(repository);
    }
}
