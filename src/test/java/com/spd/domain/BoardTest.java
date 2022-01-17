package com.spd.domain;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.BoardVisibility;
import com.spd.trello.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest extends BaseTest{


    private static BoardService boardService;

    public BoardTest() {
        boardService = new BoardService();
    }

    @BeforeEach
    public void createTestBoard() {
        board.setId(UUID.randomUUID());
        board = boardService.create(board);
    }

    @Test
    public void successCreate() {
        board.setId(UUID.randomUUID());
        Board actual = boardService.create(board);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(board.getVisibility(), actual.getVisibility()),
                () -> assertEquals(board.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(board.getArchived(), actual.getArchived()),
                () -> assertEquals(board.getName(), actual.getName()),
                () -> assertEquals(board.getDescription(), actual.getDescription()),
                () -> assertEquals(1, actual.getMembers().size())
        );
    }

    @Test
    public void successFindById(){
        Board actual = boardService.readById(BaseTest.board.getId());
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(board.getVisibility(), actual.getVisibility()),
                () -> assertEquals(board.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(board.getArchived(), actual.getArchived()),
                () -> assertEquals(board.getName(), actual.getName()),
                () -> assertEquals(board.getDescription(), actual.getDescription()),
                () -> assertEquals(1, actual.getMembers().size())
        );
    }

    @Test
    public void successUpdate() {
        board.setUpdatedBy("test");
        board.setVisibility(BoardVisibility.PRIVATE);
        board.setDescription("test desc update");
        Board actual = boardService.update(board.getId(), board);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(board.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(board.getVisibility(), actual.getVisibility()),
                () -> assertEquals(board.getDescription(), actual.getDescription())
        );
    }

    @Test
    public void successGetObjects() {
        List<Board> boards = boardService.readAll();
        assertNotEquals(0, boards.size());
    }

    @Test
    public void failDelete() {
        assertFalse(boardService.delete(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        assertTrue(boardService.delete(board.getId()));
    }
}
