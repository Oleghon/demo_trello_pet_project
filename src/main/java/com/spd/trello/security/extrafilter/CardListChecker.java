package com.spd.trello.security.extrafilter;

import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.CardList;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.CardListRepository;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Component
public class CardListChecker extends AbstractChecker<CardList, CardListRepository> {

    public CardListChecker(UserRepository userRepository, MemberRepository memberRepository, CardListRepository entityRepository) {
        super(userRepository, memberRepository, entityRepository);
    }

    @Override
    protected void checkPostRequest(HttpServletRequest request, UUID userId) {
        CardList cardList = readFromJson(request, CardList.class);
        Member member = getMemberByParent(userId, cardList.getBoardId());
        if (!member.getRole().getPermissions().contains(Permission.WRITE))
            throw new SecurityAccessException("Member does not have enough access rights");
    }

    private Member getMemberByParent(UUID userId, UUID parentId) {
        List<Member> members = memberRepository.findByUserIdAndBoardsExists(userId, parentId);
        if (!members.isEmpty())
            members.get(0);
        throw new SecurityAccessException("User with id: " + userId + " does not have access to modify board");
    }

    @Override
    protected void checkMembership(UUID entityId, User user, Permission permission) {}

    @Override
    protected Member findMemberBy(UUID entityId, UUID userId) {return null;}

    @Override
    protected void checkEntityAccessRights(UUID entityId, User user) {}
}
