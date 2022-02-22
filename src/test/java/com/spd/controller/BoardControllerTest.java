package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.enums.BoardVisibility;
import com.spd.trello.domain.resources.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
public class BoardControllerTest extends AbstractControllerTest<Board> {

    private static String URL = "/boards";

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        Board expected = new Board();
        expected.setWorkspaceId(UUID.fromString("7ee897d3-9065-471d-53bd-7ad5f30c5bd4"));
        expected.setName("board test");
        expected.setDescription("desc test");
        MvcResult result = super.create(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description")),
                () -> assertEquals(expected.getVisibility().name(), getValue(result, "$.visibility"))
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        Board expected = new Board();
        expected.setWorkspaceId(UUID.fromString("7ee897d3-9065-471d-53bd-7ad5f30c5bd4"));
        expected.setName("board test");
        expected.setDescription("desc test");
        super.create(URL, expected);

        expected.setName("board test2 ");
        expected.setDescription("desc test 2");
        expected.setVisibility(BoardVisibility.WORKSPACE);
        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description")),
                () -> assertEquals(expected.getVisibility().name(), getValue(result, "$.visibility"))
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        Board expected = new Board();
        expected.setId(UUID.fromString("7ee897d3-9065-821d-93bd-4ad6f30c5bd4"));
        MvcResult result = super.readById(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertNotNull(getValue(result, "$.name")),
                () -> assertNotNull(getValue(result, "$.description")),
                () -> assertNotNull(getValue(result, "$.visibility"))
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        Board expected = new Board();
        expected.setWorkspaceId(UUID.fromString("7ee897d3-9065-471d-53bd-7ad5f30c5bd4"));
        expected.setName("board test");
        expected.setDescription("desc test");
        super.create(URL, expected);
        MvcResult result = super.delete(URL, expected.getId());

        assertAll(
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
}