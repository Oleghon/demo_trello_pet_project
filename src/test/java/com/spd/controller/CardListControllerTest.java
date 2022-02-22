package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.resources.CardList;
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
public class CardListControllerTest extends AbstractControllerTest<CardList> {

    private static String URL = "/cardlists";

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        CardList expected = new CardList();
        expected.setBoardId(UUID.fromString("7ee897d3-9065-821d-93bd-4ad6f30c5bd4"));
        expected.setArchived(false);
        expected.setName("test column");
        MvcResult result = super.create(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getArchived(), getValue(result, "$.archived"))
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        CardList expected = new CardList();
        expected.setBoardId(UUID.fromString("7ee897d3-9065-821d-93bd-4ad6f30c5bd4"));
        expected.setArchived(false);
        expected.setName("test column");
        super.create(URL, expected);

        expected.setName("test2");
        expected.setArchived(true);

        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getArchived(), getValue(result, "$.archived"))
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        CardList expected = new CardList();
        expected.setId(UUID.fromString("7ee897d3-9065-885d-93bd-4ad6f30c5fd4"));

        MvcResult result = super.readById(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertNotNull(getValue(result, "$.name"))
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        CardList expected = new CardList();
        expected.setBoardId(UUID.fromString("7ee897d3-9065-821d-93bd-4ad6f30c5bd4"));
        expected.setArchived(false);
        expected.setName("test column");
        super.create(URL, expected);

        MvcResult result = super.delete(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getArchived(), getValue(result, "$.archived"))
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}