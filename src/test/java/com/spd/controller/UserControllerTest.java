package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.resources.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
public class UserControllerTest extends AbstractControllerTest<User> {

    private static String URL = "/users";

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        User expected = new User();
        expected.setId(UUID.fromString("7ee897d3-9045-521d-93bd-7ad5f30c3bd7"));
        expected.setFirstName("test");
        expected.setLastName("test");
        expected.setEmail("test");
        MvcResult result = super.create(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getFirstName(), getValue(result, "$.firstName")),
                () -> assertEquals(expected.getLastName(), getValue(result, "$.lastName"))
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        User expected = new User();
        expected.setId(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c3bd9"));
        expected.setFirstName("test2");
        expected.setLastName("test2");
        expected.setEmail("test2");

        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertEquals(expected.getEmail(), getValue(result, "$.email")),
                () -> assertEquals(expected.getFirstName(), getValue(result, "$.firstName")),
                () -> assertEquals(expected.getLastName(), getValue(result, "$.lastName")),
                () -> assertEquals(expected.getCreatedBy(), getValue(result, "$.createdBy"))
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        User expected = new User();
        //when(repository.save(expected)).thenReturn(expected);

        expected.setId(UUID.fromString("7ee897d3-9045-521d-94bd-7ad5f60c3bd7"));
        expected.setFirstName("test");
        expected.setLastName("test");
        expected.setEmail("test");
        expected.setCreatedBy("test");
        super.create(URL, expected);
        MvcResult result = super.readById(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getFirstName(), getValue(result, "$.firstName")),
                () -> assertEquals(expected.getLastName(), getValue(result, "$.lastName"))
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        User expected = new User();
        expected.setId(UUID.fromString("7ee897d3-9045-521d-94bd-7ad5f60c7bd2"));
        expected.setFirstName("test");
        expected.setLastName("test");
        expected.setEmail("test");
        super.create(URL, expected);

        MvcResult result = super.delete(URL, expected.getId());
        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getFirstName(), getValue(result, "$.firstName")),
                () -> assertEquals(expected.getLastName(), getValue(result, "$.lastName"))
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}

