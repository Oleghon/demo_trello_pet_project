package com.spd.trello.security.extrafilter;

import com.spd.trello.configuration.UserContextHolder;
import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.*;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CardChecker extends AbstractChecker<Card, CardRepository> {

    private CardListRepository cardListRepository;
    private BoardRepository boardRepository;

    @Autowired
    public CardChecker(UserRepository userRepository, MemberRepository memberRepository, CardRepository entityRepository, CardListRepository cardListRepository, BoardRepository boardRepository) {
        super(userRepository, memberRepository, entityRepository);
        this.cardListRepository = cardListRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    protected void checkMembership(UUID entityId, User user, Permission permission) {
        Card card = entityRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        Member member = findMemberBy(card, user);
        if (!member.getRole().getPermissions().contains(permission))
            throw new SecurityAccessException("Member does not have enough access rights");
    }

    @Override
    protected Member findMemberBy(Card entity, User user) {
        List<Member> members = UserContextHolder.getMembersContext(user.getEmail());
        return members.stream()
                .filter(member -> entity.getAssignedMembers().contains(member.getId()))
                .findFirst()
                .orElseThrow(() -> new SecurityAccessException("User: " + user.getEmail() + " does not have access to card"));
    }

    @Override
    protected void checkPostRequest(HttpServletRequest request, User user) {
        Card card = readFromJson(request, Card.class);
        if (cardListRepository.existsById(card.getCardListId())) {
            Optional<CardList> cardListOptional = cardListRepository.findById(card.getCardListId());
            if (cardListOptional.isPresent()) {
                Member member = findMemberByParent(cardListOptional.get().getBoardId(), user);
                member.getRole().getPermissions().contains(Permission.UPDATE);
            }
        }
    }

    protected Member findMemberByParent(UUID entityId, User user) {
        Board board = boardRepository.findById(entityId).orElseThrow(EntityNotFoundException::new);
        List<Member> members = UserContextHolder.getMembersContext(user.getEmail());
        return members.stream()
                .filter(member -> board.getMembers().contains(member.getId()))
                .findFirst()
                .orElseThrow(() -> new SecurityAccessException("User: " + user.getEmail() + " does not have access to create card"));
    }

    @Override
    protected void checkEntityAccessRights(UUID entityId, User user) {
    }
}
