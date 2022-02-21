package com.spd.controller;

import com.spd.trello.domain.items.Reminder;
import com.spd.trello.domain.resources.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.spd.controller.CardListControllerTest.cardList;
import static org.junit.jupiter.api.Assertions.*;

public class CardControllerTest extends AbstractControllerTest<Card> {

    private static String URL = "/cards";
    static Card card = new Card();

    @BeforeEach
    public void setUp() {
        card.setId(UUID.fromString("3ee843d3-9525-821d-13bd-6ad5f32c4bd2"));
        card.setName("test space");
        card.setCardListId(cardList.getId());
    }

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        Card expected = card;
        MvcResult result = super.create(URL, expected);
        Card actual = super.mapFromJson(result.getResponse().getContentAsString(), Card.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(actual.getCreatedDate())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        Card expected = card;
        expected.setName("test2");
        expected.setDescription("test desc");

        MvcResult result = super.update(URL, expected);
        Card actual = super.mapFromJson(result.getResponse().getContentAsString(), Card.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy())
        );
    }

    @Test
    @DisplayName("addReminder")
    public void successAddReminder() throws Exception {
        Card expected = card;
        Reminder reminder = new Reminder();
        reminder.setAlive(true);
        expected.setReminder(reminder);

        MvcResult result = super.update(URL, expected);
        Card actual = super.mapFromJson(result.getResponse().getContentAsString(), Card.class);

        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy()),
                () -> assertEquals(expected.getReminder().getId(), actual.getReminder().getId())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        Card expected = card;
        MvcResult result = super.readById(URL, expected.getId());
        Card actual = super.mapFromJson(result.getResponse().getContentAsString(), Card.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        Card expected = card;
        MvcResult result = super.delete(URL, expected.getId());
        Card actual = super.mapFromJson(result.getResponse().getContentAsString(), Card.class);

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
