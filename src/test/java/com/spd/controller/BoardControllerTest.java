package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.enums.BoardVisibility;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.exception.ErrorResponse;
import com.spd.trello.repository_jpa.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
public class BoardControllerTest extends AbstractControllerTest<Board> {

    private static String URL = "/boards";

    @Autowired
    private BoardRepository repository;

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        MvcResult result = super.getAll(URL);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus())
        );
    }


    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        Board expected = EntityBuilder.buildBoard();
        MvcResult result = super.create(URL, expected);
        Board actual = mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertEquals(expected.getVisibility().name(), actual.getVisibility().name())
        );
    }

    @Test
    @DisplayName("createWithoutMembers")
    public void failCreate() throws Exception {
        Board unexpected = EntityBuilder.buildBoard();
        unexpected.setMembers(new ArrayList<>());

        MvcResult result = super.create(URL, unexpected);
        ErrorResponse actual = mapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus()),
                () -> assertEquals("Board not valid", actual.getMessage()),
                () -> assertFalse(actual.getDetails().isEmpty())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        Board expected = EntityBuilder.getBoard(repository);
        expected.setName("board test2 ");
        expected.setDescription("desc test 2");
        expected.setVisibility(BoardVisibility.WORKSPACE);

        MvcResult result = super.update(URL, expected);
        Board actual = mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getUpdatedBy()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertEquals(expected.getVisibility().name(), actual.getVisibility().name())
        );
    }

    @Test
    @DisplayName("successArchiving")
    public void successArchiving() throws Exception {
        Board unexpected = EntityBuilder.getBoard(repository);
        unexpected.setName("board test2 ");
        unexpected.setDescription("desc test 2");
        unexpected.setArchived(true);
        unexpected.setVisibility(BoardVisibility.WORKSPACE);

        MvcResult result = super.update(URL, unexpected);
        Board actual = mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNull(actual.getUpdatedBy()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(unexpected.getArchived(), actual.getArchived()),
                () -> assertEquals(unexpected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotEquals(unexpected.getName(), actual.getName()),
                () -> assertNotEquals(unexpected.getDescription(), actual.getDescription()),
                () -> assertNotEquals(unexpected.getVisibility().name(), actual.getVisibility().name())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        Board expected = new Board();
        expected.setId(UUID.fromString("7ee897d3-9065-821d-93bd-4ad6f30c5bd4"));

        MvcResult result = super.readById(URL, expected.getId());
        Board actual = mapFromJson(result.getResponse().getContentAsString(), Board.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNotNull(actual.getName()),
                () -> assertNotNull(actual.getDescription()),
                () -> assertEquals(BoardVisibility.PUBLIC.name(), actual.getVisibility().name())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        Board expected = EntityBuilder.getBoard(repository);
        MvcResult result = super.delete(URL, expected.getId());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description")),
                () -> assertEquals(expected.getVisibility().name(), getValue(result, "$.visibility"))
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @DisplayName("failUpdate")
    public void failUpdate() throws Exception {
        MvcResult result = super.readById(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}