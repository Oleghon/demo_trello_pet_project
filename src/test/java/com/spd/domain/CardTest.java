package com.spd.domain;

import com.spd.trello.domain.Card;
import com.spd.trello.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest extends BaseTest {

    private static CardService cardService;

    public CardTest() {
        super();
        cardService = new CardService();
    }

    @BeforeEach
    public void createTestCard() {
        card.setId(UUID.randomUUID());
        card = cardService.create(card);
    }

    @Test
    public void successCreate() {
        card.setId(UUID.randomUUID());
        Card actual = cardService.create(card);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(card.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(card.getArchived(), actual.getArchived()),
                () -> assertEquals(card.getAssignedMembers().size(), actual.getAssignedMembers().size()),
                () -> assertEquals(card.getName(), actual.getName())
        );
    }

    @Test
    public void successUpdate() {
        card.setUpdatedBy("test");
        card.setName("new name");
        card.setDescription("new desc");
        card.setArchived(true);
        Card actual = cardService.update(card.getId(), card);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(card.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(card.getName(), actual.getName()),
                () -> assertTrue(card.getArchived())
        );
    }

    @Test
    public void successGetObjects() {
        List<Card> cards = cardService.readAll();
        assertNotEquals(0, cards.size());
    }

    @Test
    public void failDelete() {
        assertFalse(cardService.delete(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        assertTrue(cardService.delete(card.getId()));
    }
}
