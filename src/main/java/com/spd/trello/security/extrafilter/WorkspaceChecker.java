package com.spd.trello.security.extrafilter;

import com.spd.trello.configuration.UserContextHolder;
import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.enums.WorkSpaceVisibility;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import com.spd.trello.repository_jpa.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Component
public class WorkspaceChecker extends AbstractChecker<WorkSpace, WorkspaceRepository> {

    @Autowired
    public WorkspaceChecker(UserRepository userRepository, MemberRepository memberRepository, WorkspaceRepository entityRepository) {
        super(userRepository, memberRepository, entityRepository);
    }

    @Override
    protected void checkMembership(UUID entityId, User user, Permission permission) {
        WorkSpace workSpace = entityRepository.findById(entityId).get();
        Member member = findMemberBy(workSpace, user);
        if (!member.getRole().getPermissions().contains(permission))
            throw new SecurityAccessException("Member does not have enough access rights to modify workspace");
    }

    @Override
    protected Member findMemberBy(WorkSpace workSpace, User user) {
        List<Member> members = UserContextHolder.getMembersContext(user.getEmail());
        return members.stream()
                .filter(member -> workSpace.getWorkspaceMembers().contains(member.getId()))
                .findFirst()
                .orElseThrow(() -> new SecurityAccessException("User: " + user.getEmail() + " does not have access to workspace"));
    }

    @Override
    protected void checkEntityAccessRights(UUID entityId, User user) {
        WorkSpace workSpace = entityRepository.findById(entityId).get();
        if (workSpace.getVisibility() == WorkSpaceVisibility.PRIVATE)
            findMemberBy(workSpace, user);
    }

    @Override
    protected void checkPostRequest(HttpServletRequest request, User userId) {
    }
}
