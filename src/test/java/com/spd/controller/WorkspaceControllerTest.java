package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.resources.WorkSpace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
public class WorkspaceControllerTest extends AbstractControllerTest<WorkSpace> {

    private static String URL = "/workspaces";


    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        WorkSpace expected = new WorkSpace();
        expected.setName("test");
        expected.setDescription("test desc");
        expected.setWorkspaceMembers(Set.of(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c5bd4")));

        MvcResult result = super.create(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description"))
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        WorkSpace expected = new WorkSpace();
        expected.setName("test");
        expected.setDescription("test desc");
        expected.setWorkspaceMembers(Set.of(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c5bd4")));
        super.create(URL, expected);

        expected.setName("test2");
        expected.setDescription("test2");

        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description"))
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        WorkSpace expected = new WorkSpace();
        expected.setId(UUID.fromString("7ee897d3-9065-471d-53bd-7ad5f30c5bd4"));

        MvcResult result = super.readById(URL, expected.getId());
        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy"))
        );

    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        WorkSpace expected = new WorkSpace();
        expected.setName("test");
        expected.setDescription("test desc");
        expected.setWorkspaceMembers(Set.of(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c5bd4")));
        super.create(URL, expected);
        MvcResult result = super.delete(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getName(), getValue(result, "$.name")),
                () -> assertEquals(expected.getDescription(), getValue(result, "$.description"))
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}