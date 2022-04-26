package com.spd.trello.security.extrafilter;

import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.BoardRepository;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Component
public class BoardChecker extends AbstractChecker<Board, BoardRepository> {

    @Autowired
    public BoardChecker(UserRepository userRepository, MemberRepository memberRepository, BoardRepository entityRepository) {
        super(userRepository, memberRepository, entityRepository);
    }

    @Override
    protected void checkEntityAccessRights(UUID entityId, User user) {
        Board board = entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        switch (board.getVisibility().name()) {
            case "WORKSPACE":
            case "PRIVATE":
                findMemberBy(entityId, user.getId());
                break;
        }
    }

    @Override
    protected void checkMembership(UUID entityId, User user, Permission permission) {
        Member member = findMemberBy(entityId, user.getId());
        if (!member.getRole().getPermissions().contains(permission))
            throw new SecurityAccessException("Member does not have enough access rights");
    }

    @Override
    protected Member findMemberBy(UUID entityId, UUID userId) {
        List<Member> members = memberRepository.findByUserIdAndBoardsExists(userId, entityId);
        if (!members.isEmpty())
            return members.get(0);
        throw new SecurityAccessException("User with id: " + userId + " does not have access to modify board");
    }

    protected Member findMemberByParent(UUID entityId, UUID userId) {
        List<Member> members = memberRepository.findByUserIdAndWorkspacesExists(userId, entityId);
        if (!members.isEmpty())
           return members.get(0);
        throw new SecurityAccessException("User with id: " + userId + " does not have access to modify board");
    }

    @Override
    protected void checkPostRequest(HttpServletRequest request, UUID userId) {
        Board board = readFromJson(request, Board.class);
        Member member = findMemberByParent(board.getWorkspaceId(), userId);
        if (!member.getRole().getPermissions().contains(Permission.WRITE))
            throw new SecurityAccessException("Member does not have enough access rights");
    }
}
