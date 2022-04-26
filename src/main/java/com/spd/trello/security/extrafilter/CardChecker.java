package com.spd.trello.security.extrafilter;

import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.CardRepository;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Component
public class CardChecker extends AbstractChecker<Card, CardRepository> {

    public CardChecker(UserRepository userRepository, MemberRepository memberRepository, CardRepository entityRepository) {
        super(userRepository, memberRepository, entityRepository);
    }

    @Override
    protected void checkMembership(UUID entityId, User user, Permission permission) {
        Member member = findMemberBy(entityId, user.getId());
        if (!member.getRole().getPermissions().contains(permission))
            throw new SecurityAccessException("Member does not have enough access rights");
    }

    @Override
    protected Member findMemberBy(UUID entityId, UUID userId) {
        List<Member> members = memberRepository.findByUserIdAndCardsExists(userId, entityId);
        if (!members.isEmpty())
            return members.get(0);
        throw new SecurityAccessException("User with id: " + userId + " does not have access to card");
    }

    @Override
    protected void checkEntityAccessRights(UUID entityId, User user) {
    }

    @Override
    protected void checkPostRequest(HttpServletRequest request, UUID userId) {
    }
}
