package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.resources.CardList;
import com.spd.trello.repository_jpa.CardListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    CardListRepository repository;


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
        CardList expected = EntityBuilder.buildCardList();
        MvcResult result = super.create(URL, expected);
        CardList actual = mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        CardList expected = EntityBuilder.getCardList(repository);
        expected.setName("test2");

        MvcResult result = super.update(URL, expected);
        CardList actual = mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotNull(actual.getUpdatedBy()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("successArchiving")
    public void successArchiving() throws Exception {
        CardList unexpected = EntityBuilder.getCardList(repository);
        unexpected.setName("test2");
        unexpected.setArchived(true);

        MvcResult result = super.update(URL, unexpected);
        CardList actual = mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNull(actual.getUpdatedBy()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(unexpected.getArchived(), actual.getArchived()),
                () -> assertEquals(unexpected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotEquals(unexpected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        CardList expected = new CardList();
        expected.setId(UUID.fromString("7ee897d3-9065-885d-93bd-4ad6f30c5fd4"));

        MvcResult result = super.readById(URL, expected.getId());
        CardList actual = mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNotNull(actual.getName())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        CardList expected = EntityBuilder.getCardList(repository);
        MvcResult result = super.delete(URL, expected.getId());
        CardList actual = mapFromJson(result.getResponse().getContentAsString(), CardList.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}