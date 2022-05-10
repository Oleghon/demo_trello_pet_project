package com.spd.trello.security.extrafilter;

import com.spd.trello.configuration.UserContextHolder;
import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.CardList;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.BoardRepository;
import com.spd.trello.repository_jpa.CardListRepository;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Component
public class CardListChecker extends AbstractChecker<CardList, CardListRepository> {

    private BoardRepository boardRepository;

    public CardListChecker(UserRepository userRepository, MemberRepository memberRepository, CardListRepository entityRepository, BoardRepository boardRepository) {
        super(userRepository, memberRepository, entityRepository);
        this.boardRepository = boardRepository;
    }

    @Override
    protected void checkPostRequest(HttpServletRequest request, User user) {
        CardList cardList = readFromJson(request, CardList.class);
        Member member = getMemberByParent(user, cardList.getBoardId());
        if (!member.getRole().getPermissions().contains(Permission.UPDATE))
            throw new SecurityAccessException("Member does not have enough access rights");
    }

    private Member getMemberByParent(User user, UUID parentId) {
        Board board = boardRepository.findById(parentId).orElseThrow(EntityNotFoundException::new);
        List<Member> members = UserContextHolder.getMembersContext(user.getEmail());
        return members.stream()
                .filter(member -> board.getMembers().contains(member.getId()))
                .findFirst()
                .orElseThrow(() -> new SecurityAccessException("User: " + user.getEmail() + " does not have access to workspace"));
    }

    @Override
    protected void checkMembership(UUID entityId, User user, Permission permission) {}

    @Override
    protected Member findMemberBy(CardList entity, User user) {
        return null;
    }

    @Override
    protected void checkEntityAccessRights(UUID entityId, User user) {}
}
