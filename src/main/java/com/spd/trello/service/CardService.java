package com.spd.trello.service;

import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.repository_jpa.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CardService extends ArchivedResourceService<Card, CardRepository> {

    private MemberService memberService;

    public CardService(CardRepository repository, MemberService memberService) {
        super(repository);
        this.memberService = memberService;
    }

    public Card addMember(UUID id, Member member) {
        Card card = super.readById(id);
        Member checkedMember = memberService.findByUserIdAndRole(member.getUserId(), member.getRole());
        card.getAssignedMembers().add(checkedMember.getId());
        log.info("member successfully added to card");
        return super.create(card);
    }
}
