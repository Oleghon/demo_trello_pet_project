package com.spd.controller;

import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.items.CheckableItem;
import com.spd.trello.domain.items.Reminder;
import com.spd.trello.domain.resources.*;
import com.spd.trello.repository_jpa.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EntityBuilder {


    static User buildUser() {
        User user = new User();
        user.setFirstName("test");
        user.setEmail("test@email.com");
        user.setLastName("test");
        user.setCreatedBy("test");
        user.setCreatedDate(LocalDateTime.now());
        return user;
    }

    static User getUser(UserRepository userRepository) {
        return userRepository.save(buildUser());
    }

    static Member buildMember() {
        Member member = new Member();
        User user = new User();
        user.setId(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c3bd9"));
        member.setUser(user);
        member.setRole(Role.ADMIN);
        member.setCreatedDate(LocalDateTime.now());
        member.setCreatedBy("test");
        return member;
    }

    static Member getMember(MemberRepository repository) {
        return repository.save(buildMember());
    }

    static WorkSpace buildWorkSpace() {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setName("test");
        workSpace.setDescription("test desc");
        workSpace.setWorkspaceMembers(Set.of(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c5bd4")));
        workSpace.setCreatedDate(LocalDateTime.now());
        workSpace.setCreatedBy("test");
        return workSpace;
    }

    static WorkSpace getWorkSpace(WorkspaceRepository repository) {
        return repository.save(buildWorkSpace());
    }

    static Board buildBoard() {
        Board board = new Board();
        board.setWorkspaceId(UUID.fromString("7ee897d3-9065-471d-53bd-7ad5f30c5bd4"));
        board.setName("board test");
        board.setDescription("desc test");
        board.setCreatedDate(LocalDateTime.now());
        board.setMembers(List.of(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c5bd4")));
        board.setCreatedBy("test");
        return board;
    }

    static Board getBoard(BoardRepository repository) {
        return repository.save(buildBoard());
    }

    static CardList buildCardList() {
        CardList cardList = new CardList();
        cardList.setBoardId(UUID.fromString("7ee897d3-9065-821d-93bd-4ad6f30c5bd4"));
        cardList.setArchived(false);
        cardList.setName("test column");
        cardList.setCreatedDate(LocalDateTime.now());
        cardList.setCreatedBy("test");
        return cardList;
    }

    static CardList getCardList(CardListRepository repository) {
        return repository.save(buildCardList());
    }

    static Card buildCard() {
        Card card = new Card();
        card.setCardListId(UUID.fromString("7ee897d3-9065-885d-93bd-4ad6f30c5fd4"));
        card.setName("test");
        card.setArchived(false);
        card.setDescription("card desc");
        card.setCreatedDate(LocalDateTime.now());
        card.setCreatedBy("test");
        card.setAssignedMembers(Set.of(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c5bd4")));
        return card;
    }

    static Card getCard(CardRepository repository) {
        return repository.save(buildCard());
    }

    static Card getCard(CardRepository repository, boolean flag){
        Card card = buildCard();
        card.setArchived(flag);
        return repository.save(card);
    }

    static CheckList buildCheckList() {
        CheckList checkList = new CheckList();
        CheckableItem check = new CheckableItem();
        check.setCheck(true);
        check.setName("test check");
        check.setCheckList(checkList);

        checkList.setName("test");
        checkList.setCreatedDate(LocalDateTime.now());
        checkList.setItems(List.of(check));
        return checkList;
    }

    static Reminder buildReminder(){
        Reminder reminder = new Reminder();
        reminder.setStart(LocalDateTime.now().plusSeconds(15));
        reminder.setRemindOn(LocalDateTime.now().plusMinutes(5));
        reminder.setEnd(LocalDateTime.now().plusHours(1));
        return reminder;
    }

    static Card getCardWithCheckList(CardRepository repository) {
        Card card = buildCard();
        card.setCheckList(buildCheckList());
        return repository.save(card);
    }
}
