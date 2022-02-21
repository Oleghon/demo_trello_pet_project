package com.spd.controller;

import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.resources.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.spd.controller.UserControllerTest.user;
import static org.junit.jupiter.api.Assertions.*;

public class MemberControllerTest extends AbstractControllerTest<Member> {

    private static String URL = "/members";
    static Member member = new Member();

    @BeforeEach
    public void setUp() {
        member.setId(UUID.fromString("7ee897d3-9045-521d-93bd-6ad5f30c4bd7"));
        member.setUser(user);
    }

    @Test
    @DisplayName("readAll")
    public void successReadAll() throws Exception {
        super.getAll(URL);
    }

    @Test
    @DisplayName("create")
    public void successCreate() throws Exception {
        Member expected = member;
        MvcResult result = super.create(URL, expected);
        Member actual = super.mapFromJson(result.getResponse().getContentAsString(), Member.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotNull(actual.getCreatedDate())
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        Member expected = member;
        expected.setRole(Role.ADMIN);

        MvcResult result = super.update(URL, expected);
        Member actual = super.mapFromJson(result.getResponse().getContentAsString(), Member.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getUpdatedBy())
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        Member expected = member;
        MvcResult result = super.readById(URL, expected.getId());
        Member actual = super.mapFromJson(result.getResponse().getContentAsString(), Member.class);

        assertAll(
                () -> assertEquals(expected.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(expected.getRole(), actual.getRole())
        );
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        Member expected = member;
        MvcResult result = super.delete(URL, expected.getId());
        Member actual = super.mapFromJson(result.getResponse().getContentAsString(), Member.class);

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
