package com.spd.trello.security.extrafilter;

import com.spd.trello.configuration.UserContextHolder;
import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.BoardRepository;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import com.spd.trello.repository_jpa.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Component
public class BoardChecker extends AbstractChecker<Board, BoardRepository> {

    private WorkspaceRepository workspaceRepository;

    @Autowired
    public BoardChecker(UserRepository userRepository, MemberRepository memberRepository, BoardRepository entityRepository, WorkspaceRepository workspaceRepository) {
        super(userRepository, memberRepository, entityRepository);
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    protected void checkEntityAccessRights(UUID entityId, User user) {
        Board board = entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        if (!board.getVisibility().name().equals("PUBLIC")) {
            findMemberBy(board, user);
        }
    }

    @Override
    protected void checkMembership(UUID entityId, User user, Permission permission) {
        Board board = entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        Member member = findMemberBy(board, user);
        if (!member.getRole().getPermissions().contains(permission))
            throw new SecurityAccessException("Member does not have enough access rights");
    }

    @Override
    protected Member findMemberBy(Board entity, User user) {
        List<Member> members = UserContextHolder.getMembersContext(user.getEmail());
        return members.stream()
                .filter(member -> entity.getMembers().contains(member.getId()))
                .findFirst()
                .orElseThrow(() -> new SecurityAccessException("User: " + user.getEmail() + " does not have access to board"));
    }

    protected Member findMemberByParent(UUID entityId, User user) {
        WorkSpace workSpace = workspaceRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        List<Member> members = UserContextHolder.getMembersContext(user.getEmail());
        return members.stream()
                .filter(member -> workSpace.getWorkspaceMembers().contains(member.getId()))
                .findFirst()
                .orElseThrow(() -> new SecurityAccessException("User: " + user.getEmail() + " does not have access to modify workspace"));
    }

    @Override
    protected void checkPostRequest(HttpServletRequest request, User user) {
        Board board = readFromJson(request, Board.class);
        Member member = findMemberByParent(board.getWorkspaceId(), user);
        if (!member.getRole().getPermissions().contains(Permission.WRITE))
            throw new SecurityAccessException("Member does not have enough access rights");
    }
}
