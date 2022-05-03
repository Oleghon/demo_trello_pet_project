package com.spd.trello.service;

import com.spd.trello.configuration.UserContextHolder;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.repository_jpa.WorkspaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class WorkSpaceService extends AbstractService<WorkSpace, WorkspaceRepository> {
    private MemberService memberService;

    public WorkSpaceService(WorkspaceRepository repository, MemberService memberService) {
        super(repository);
        this.memberService = memberService;
    }

    public WorkSpace addMember(UUID id, Member member) {
        WorkSpace workSpace = super.readById(id);
        Member checkedMember = memberService.findByUserIdAndRole(member.getUserId(), member.getRole());
        workSpace.getWorkspaceMembers().add(checkedMember.getId());
        log.info("member successfully added to workspace");
        return super.create(workSpace);
    }

    @Override
    public Page<WorkSpace> readAll(Pageable pageable) {
        Set<WorkSpace> workSpaceList = new HashSet<>();
        List<UUID> membersIdContext = UserContextHolder.getMembersIdContext(SecurityContextHolder.getContext().getAuthentication().getName());
        membersIdContext.forEach(membersId -> {
            workSpaceList.addAll(repository.findAllByMemberId(membersId));
        });
        return new PageImpl<>(new ArrayList<>(workSpaceList), pageable, workSpaceList.size());
    }
}
