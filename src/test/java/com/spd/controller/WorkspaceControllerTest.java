package com.spd.controller;

import com.spd.trello.domain.resources.WorkSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;
import java.util.UUID;

import static com.spd.controller.MemberControllerTest.member;
import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceControllerTest extends AbstractControllerTest<WorkSpace> {

    private static String URL = "/workspaces";
    static WorkSpace space = new WorkSpace();

    @BeforeEach
    public void setUp() {
        space.setId(UUID.fromString("7ee847d3-9845-521d-13bd-6ad5f30c4bd7"));
        space.setName("test space");
        space.setDescription("test desc");
    }

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        WorkSpace expected = space;
        MvcResult result = super.create(URL, expected);
        WorkSpace actual = super.mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(actual.getCreatedDate())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        WorkSpace expected = space;
        expected.setWorkspaceMembers(Set.of(member.getId()));

        MvcResult result = super.update(URL, expected);
        WorkSpace actual = super.mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        WorkSpace expected = space;
        MvcResult result = super.readById(URL, expected.getId());
        WorkSpace actual = super.mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        WorkSpace expected = space;
        MvcResult result = super.delete(URL, expected.getId());
        WorkSpace actual = super.mapFromJson(result.getResponse().getContentAsString(), WorkSpace.class);

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