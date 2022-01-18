package com.spd.trello.service;

import com.spd.trello.domain.Member;
import com.spd.trello.domain.Role;
import com.spd.trello.domain.User;
import com.spd.trello.repository.impl.MemberRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MemberService extends AbstractService<Member> {

    private MemberRepositoryImpl repository;

    public MemberService() {
        repository = new MemberRepositoryImpl();
    }

    public Member create(User user, Role role, String createdBy) {
        Member member = new Member();
        member.setCreatedBy(createdBy);
        member.setUser(user);
        member.setRole(role);
        return create(member);
    }

    @Override
    public Member create(Member obj) {
        repository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public Member update(UUID id, Member obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        repository.update(id, obj);
        return readById(id);
    }

    @Override
    public Member readById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return repository.delete(id);
    }

    @Override
    public List<Member> readAll() {
        return repository.getObjects();
    }
}
