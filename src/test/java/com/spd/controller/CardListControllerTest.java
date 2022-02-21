package com.spd.controller;

import com.spd.trello.domain.resources.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.spd.controller.BoardControllerTest.board;
import static org.junit.jupiter.api.Assertions.*;

public class CardListControllerTest extends AbstractControllerTest<CardList> {

    private static String URL = "/cardlists";
    static CardList cardList = new CardList();

    @BeforeEach
    public void setUp() {
        cardList.setId(UUID.fromString("3ee843d3-9525-821d-13bd-6ad5f32c4bd2"));
        cardList.setName("test space");
        cardList.setBoardId(board.getId());
    }

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        CardList expected = cardList;
        MvcResult result = super.create(URL, expected);
        CardList actual = super.mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(actual.getCreatedDate())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        CardList expected = cardList;
        expected.setName("test2");

        MvcResult result = super.update(URL, expected);
        CardList actual = super.mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        CardList expected = cardList;
        MvcResult result = super.readById(URL, expected.getId());
        CardList actual = super.mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        CardList expected = cardList;
        MvcResult result = super.delete(URL, expected.getId());
        CardList actual = super.mapFromJson(result.getResponse().getContentAsString(), CardList.class);

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