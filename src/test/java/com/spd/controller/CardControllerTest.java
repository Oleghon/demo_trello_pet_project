package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.items.Reminder;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.resources.CheckList;
import com.spd.trello.exception.ErrorResponse;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import static com.spd.controller.EntityBuilder.buildCheckList;
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
        Card actual = mapFromJson(result.getResponse().getContentAsString(), Card.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {

        Card expected = EntityBuilder.getCard(repository);
        expected.setName("test2");
        expected.setDescription("new desc");
        MvcResult result = super.update(URL, expected);
        Card actual = mapFromJson(result.getResponse().getContentAsString(), Card.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription())
        );
    }

    @Test
    @DisplayName("createWithoutMembers")
    public void failCreate() throws Exception {
        Card unexpected = EntityBuilder.buildCard();
        unexpected.setAssignedMembers(new HashSet<>());

        MvcResult result = super.create(URL, unexpected);
        ErrorResponse actual = mapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus()),
                () -> assertEquals("Card not valid", actual.getMessage()),
                () -> assertFalse(actual.getDetails().isEmpty())
        );
    }

    @Test
    @DisplayName("failUpdateArchivedCard")
    public void failUpdate() throws Exception {
        Card expected = EntityBuilder.getCard(repository, true);
        expected.setName("test2");
        expected.setDescription("new desc");
        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus()),
                () -> assertEquals("Archived Card can not be updated", result.getResponse().getContentAsString())
        );
    }

    @Test
    @DisplayName("successArchiving")
    public void successArchiving() throws Exception {
        Card unexpected = EntityBuilder.getCard(repository);
        unexpected.setName("test2 ");
        unexpected.setDescription("desc test 2");
        unexpected.setArchived(true);

        MvcResult result = super.update(URL, unexpected);
        Card actual = mapFromJson(result.getResponse().getContentAsString(), Card.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNull(actual.getUpdatedBy()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(unexpected.getArchived(), actual.getArchived()),
                () -> assertEquals(unexpected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotEquals(unexpected.getName(), actual.getName()),
                () -> assertNotEquals(unexpected.getDescription(), actual.getDescription())
        );
    }

    @Test
    @DisplayName("addReminder")
    public void successAddReminder() throws Exception {
        Card expected = EntityBuilder.getCard(repository);
        Reminder reminder = EntityBuilder.buildReminder();
        expected.setReminder(reminder);

        MvcResult result = super.update(URL, expected);
        Reminder actual = mapFromJson(result.getResponse().getContentAsString(), Card.class).getReminder();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(reminder.getStart(), actual.getStart()),
                () -> assertEquals(reminder.getEnd(), actual.getEnd()),
                () -> assertEquals(reminder.getRemindOn(), actual.getRemindOn())
        );
    }

    @Test
    @DisplayName("addCheckList")
    public void successAddCheckList() throws Exception {

        Card expected = EntityBuilder.getCard(repository);
        CheckList checkList = buildCheckList();
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
    @DisplayName("failDeleteCard")
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
