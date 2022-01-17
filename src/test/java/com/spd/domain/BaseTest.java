package com.spd.domain;

import com.spd.trello.domain.*;
import com.spd.trello.service.*;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;

public class BaseTest {


    protected static User user;
    protected static Member member;
    protected static WorkSpace workSpace;
    protected static Board board;
    protected static CardList cardList;
    protected static Card card;

    @BeforeAll
    public static void createEntities() {
        user = new UserService().create("test", "test", "test", "test");
        member = new MemberService().create(user, Role.ADMIN, "test");
        workSpace = new WorkSpaceService().create("test", "test name",
                WorkSpaceVisibility.PUBLIC, "test", new ArrayList<>() {{
                    add(member);
                }}, new ArrayList<>());

        board = new Board();
        board.setCreatedBy("test");
        board.setName("test board");
        board.setDescription("test desc");
        board.setWorkSpace(workSpace);
        board.setVisibility(BoardVisibility.PUBLIC);
        board.setArchived(false);
        board.setMembers(new ArrayList<>() {{
            add(member);
        }});
        board = new BoardService().create(board);

        cardList = new CardList();
        cardList.setBoard(board);
        cardList.setCreatedBy("test");
        cardList.setName("test list");
        cardList.setArchived(false);
        cardList = new CardListService().create(cardList);

        card = new Card();
        card.setCreatedBy("test");
        card.setName("test list");
        card.setDescription("test desc");
        card.setAssignedMembers(new ArrayList<>() {{
            add(member);
        }});
        card.setArchived(false);
        card.setCardList(cardList);
        card = new CardService().create(card);
    }
}
