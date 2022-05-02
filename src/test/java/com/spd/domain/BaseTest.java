package com.spd.domain;

import com.spd.trello.domain.enums.BoardVisibility;
import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.enums.WorkSpaceVisibility;
import com.spd.trello.domain.resources.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class BaseTest {

    public static User user;
    public static Member member;
    public static WorkSpace workSpace;
    public static Board board;
    public static CardList cardList;
    public static Card card;
    public static Pageable pageable = PageRequest.of(0, 2);


    @BeforeAll
    public static void createEntities() {
        user = new User();
        user.setId(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c3bd9"));
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test@test.com");

        member = new Member();
        member.setId(UUID.fromString("7ee897d3-9065-421d-93bd-7ad7f30c4bd9"));
        member.setUserId(user.getId());
        member.setRole(Role.ADMIN);
        workSpace = new WorkSpace();

        workSpace.setName("test");
        workSpace.setDescription("test name");
        workSpace.setVisibility(WorkSpaceVisibility.PUBLIC);

        board = new Board();
        board.setId(UUID.fromString("7ee897d3-9045-521d-93bd-7ad5f30c3bd9"));
        board.setCreatedBy("test");
        board.setName("test board");
        board.setDescription("test desc");
        board.setWorkspaceId(workSpace.getId());
        board.setVisibility(BoardVisibility.PUBLIC);
        board.setArchived(false);

        cardList = new CardList();
        cardList.setId(UUID.fromString("7ee597d3-9365-421d-96bd-7ad5f30c3bd9"));
        cardList.setBoardId(board.getId());
        cardList.setCreatedBy("test");
        cardList.setName("test list");
        cardList.setArchived(false);

        card = new Card();
        card.setId(UUID.fromString("7ee897d3-9061-421d-03bd-7ad5f30c7bd9"));
        card.setCreatedBy("test");
        card.setName("test list");
        card.setDescription("test desc");
    }
}
