package com.spd.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spd.trello.Application;
import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.exception.ErrorResponse;
import com.spd.trello.repository_jpa.WorkspaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
public class WorkspaceControllerTest extends AbstractControllerTest<WorkSpace> {

    private static String URL = "/workspaces";

    @Autowired
    private WorkspaceRepository repository;

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
        WorkSpace expected = EntityBuilder.buildWorkSpace();
        MvcResult result = super.create(URL, expected);
        WorkSpace actual = mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertFalse(actual.getWorkspaceMembers().isEmpty())
        );
    }

    @Test
    @DisplayName("createWithoutMembers")
    public void failCreate() throws Exception {
        WorkSpace unexpected = EntityBuilder.buildWorkSpace();
        unexpected.setWorkspaceMembers(new HashSet<>());

        MvcResult result = super.create(URL, unexpected);
        ErrorResponse actual = mapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus()),
                () -> assertEquals("WorkSpace not valid", actual.getMessage()),
                () -> assertFalse(actual.getDetails().isEmpty())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        WorkSpace expected = EntityBuilder.getWorkSpace(repository);
        expected.setName("test2");
        expected.setDescription("test2");

        MvcResult result = super.update(URL, expected);
        WorkSpace actual = mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

        assertAll(
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy()),
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getCreatedDate(), actual.getCreatedDate()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertFalse(actual.getWorkspaceMembers().isEmpty())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        WorkSpace expected = new WorkSpace();
        expected.setId(UUID.fromString("7ee897d3-9065-471d-53bd-7ad5f30c5bd4"));

        MvcResult result = super.readById(URL, expected.getId());
        WorkSpace actual = mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate())
        );
    }

    @Test
    @DisplayName("failReadById")
    public void failReadById() throws Exception {
        MvcResult result = super.readById(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        WorkSpace expected = EntityBuilder.getWorkSpace(repository);

        MvcResult result = super.delete(URL, expected.getId());
        WorkSpace actual = mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()),
                () -> assertNotNull(actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertFalse(actual.getWorkspaceMembers().isEmpty())
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}