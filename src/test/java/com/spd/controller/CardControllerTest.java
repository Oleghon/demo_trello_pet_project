package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.items.Reminder;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.CheckList;
import com.spd.trello.repository_jpa.CardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
public class CardControllerTest extends AbstractControllerTest<Card> {

    private static String URL = "/cards";

    @Autowired
    CardRepository repository;

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
        Card expected = EntityBuilder.buildCard();
        MvcResult result = super.create(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description")),
                () -> assertEquals(expected.getArchived(), getValue(result, "$.archived"))
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {

        Card expected = EntityBuilder.getCard(repository);

        expected.setName("test2");
        expected.setDescription("new desc");
        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description")),
                () -> assertEquals(expected.getArchived(), getValue(result, "$.archived"))
        );
    }

    @Test
    @DisplayName("addReminder")
    public void successAddReminder() throws Exception {

        Card expected = EntityBuilder.getCard(repository);

        Reminder reminder = new Reminder();
        reminder.setAlive(true);
        expected.setReminder(reminder);

        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertNotNull(getValue(result, "$.reminder")),
                () -> assertEquals(true, getValue(result, "$.reminder.alive"))
        );
    }

    @Test
    @DisplayName("addCheckList")
    public void successAddCheckList() throws Exception {

        Card expected = EntityBuilder.getCard(repository);
        CheckList checkList = new CheckList();
        checkList.setName("checkList test");
        checkList.setCreatedBy("vova");
        checkList.setCreatedDate(LocalDateTime.now());
        expected.setCheckList(checkList);

        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertNotNull(getValue(result, "$.checkList")),
                () -> assertEquals(expected.getCheckList().getName(), getValue(result, "$.checkList.name"))
        );
    }

    @Test
    @DisplayName("deleteCheckList")
    public void successDeleteCheckList() throws Exception {

        Card expected = EntityBuilder.getCardWithCheckList(repository);
        expected.setCheckList(null);
        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertNull(getValue(result, "$.checkList"))
        );
    }

    @Test
    @DisplayName("deleteCheckItem")
    public void successDeleteCheckItem() throws Exception {

        Card expected = EntityBuilder.getCardWithCheckList(repository);
        expected.getCheckList().setItems(new ArrayList<>());
        MvcResult result = super.update(URL, expected);
        ArrayList value = (ArrayList) getValue(result, "$.checkList.items");
        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertNotNull(getValue(result, "$.checkList")),
                () -> assertEquals(new ArrayList<>().size(), value.size())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        Card expected = new Card();
        expected.setId(UUID.fromString("7ee897d3-9535-885d-93bd-4ad6f36c5fd4"));

        MvcResult result = super.readById(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.id")),
                () -> assertNotNull(getValue(result, "$.reminder")),
                () -> assertNotNull(getValue(result, "$.reminder.alive")),
                () -> assertNotNull(getValue(result, "$.name")),
                () -> assertNotNull(getValue(result, "$.description")),
                () -> assertNotNull(getValue(result, "$.archived"))
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        Card expected = EntityBuilder.getCard(repository);
        MvcResult result = super.delete(URL, expected.getId());

        assertAll(
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description")),
                () -> assertEquals(expected.getArchived(), getValue(result, "$.archived"))
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @DisplayName("failFindById")
    public void failFindById() throws Exception {
        MvcResult result = super.readById(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
