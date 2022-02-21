package com.spd.controller;

import com.spd.trello.domain.resources.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static com.spd.controller.MemberControllerTest.member;
import static com.spd.controller.WorkspaceControllerTest.space;
import static org.junit.jupiter.api.Assertions.*;

public class BoardControllerTest extends AbstractControllerTest<Board> {

    private static String URL = "/boards";
    static Board board = new Board();

    @BeforeEach
    public void setUp() {
        board.setId(UUID.fromString("3ee847d3-9555-521d-13bd-6ad5f30c4bd2"));
        board.setName("test space");
        board.setDescription("test desc");
        board.setWorkspaceId(space.getId());
    }

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        Board expected = board;
        MvcResult result = super.create(URL, expected);
        Board actual = super.mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(actual.getCreatedDate())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        Board expected = board;
        expected.setMembers(List.of(member.getId()));

        MvcResult result = super.update(URL, expected);
        Board actual = super.mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        Board expected = board;
        MvcResult result = super.readById(URL, expected.getId());
        Board actual = super.mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        Board expected = board;
        MvcResult result = super.delete(URL, expected.getId());
        Board actual = super.mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus())
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}