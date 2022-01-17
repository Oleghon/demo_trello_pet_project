package com.spd.domain;

import com.spd.trello.domain.CardList;
import com.spd.trello.service.CardListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CardListTest extends BaseTest {
    private static CardListService cardListService;

    public CardListTest() {
        cardListService = new CardListService();
    }

    @BeforeEach
    public void createTestBoard() {
        cardList.setId(UUID.randomUUID());
        cardList = cardListService.create(cardList);
    }

    @Test
    public void successCreate() {
        cardList.setId(UUID.randomUUID());
        CardList actual = cardListService.create(cardList);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(cardList.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(cardList.getArchived(), actual.getArchived()),
                () -> assertEquals(cardList.getName(), actual.getName())
        );
    }

    @Test
    public void successUpdate() {
        cardList.setUpdatedBy("test");
        cardList.setName("new name");
        cardList.setArchived(true);
        CardList actual = cardListService.update(cardList.getId(), cardList);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(cardList.getUpdatedDate(), actual.getUpdatedDate()),
                () -> assertEquals(cardList.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(cardList.getName(), actual.getName()),
                () -> assertTrue(cardList.getArchived())
        );
    }

    @Test
    public void successGetObjects() {
        List<CardList> cardLists = cardListService.readAll();
        assertNotEquals(0, cardLists.size());
    }

    @Test
    public void failDelete() {
        assertFalse(cardListService.delete(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        assertTrue(cardListService.delete(cardList.getId()));
    }
}
