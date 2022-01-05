package com.spd.trello.service;

import com.spd.trello.domain.Member;
import com.spd.trello.domain.Role;
import com.spd.trello.domain.User;
import com.spd.trello.repository.impl.MemberRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MemberService extends AbstractService<Member>{

    public MemberService() {
        repository = new MemberRepositoryImpl();
    }

    public Member create(User user, Role role, String createdBy){
        Member member =new Member();
        member.setCreatedBy(createdBy);
        member.setUser(user);
        member.setRole(role);
        return create(member);
    }

    @Override
    public Member create(Member obj) {
        return repository.create(obj);
    }

    @Override
    public Member update(UUID id, Member obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        return repository.update(id, obj);
    }

    @Override
    public Member readById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        repository.delete(id);
        return false;
    }

    @Override
    public List<Member> readAll() {
        return repository.getObjects();
    }
}
