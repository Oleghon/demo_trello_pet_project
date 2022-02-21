package com.spd.controller;

import com.spd.trello.domain.resources.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class UserControllerTest extends AbstractControllerTest<User> {

    private static String URL = "/users";
    static User user = new User();

    @BeforeEach
    public void setUp() {
        user.setId(UUID.fromString("7ee897d3-9045-521d-93bd-7ad5f30c3bd7"));
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test");
    }

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        User expected = user;
        MvcResult result = super.create(URL, expected);
        User actual = super.mapFromJson(result.getResponse().getContentAsString(), User.class);

        assertAll(
                () -> assertNotNull(expected.getFirstName()),
                () -> assertEquals(expected.getFirstName(), actual.getFirstName()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        User expected = user;
        expected.setEmail("test2");

        MvcResult result = super.update(URL, expected);
        User actual = super.mapFromJson(result.getResponse().getContentAsString(), User.class);

        assertAll(
                () -> assertNotNull(expected.getFirstName()),
                () -> assertEquals(expected.getEmail(), actual.getEmail()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        User expected = user;
        MvcResult result = super.readById(URL, expected.getId());
        User actual = super.mapFromJson(result.getResponse().getContentAsString(), User.class);

        assertAll(
                () -> assertNotNull(expected.getFirstName()),
                () -> assertEquals(expected.getFirstName(), actual.getFirstName()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        User expected = user;
        MvcResult result = super.delete(URL, expected.getId());
        User actual = super.mapFromJson(result.getResponse().getContentAsString(), User.class);

        assertAll(
                () -> assertNotNull(expected.getFirstName()),
                () -> assertEquals(expected.getFirstName(), actual.getFirstName()),
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

