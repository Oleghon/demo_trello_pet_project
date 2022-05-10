package com.spd.controller;

import com.spd.trello.Application;
import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MemberControllerTest extends AbstractControllerTest<Member> {

    private static String URL = "/members";

    @Autowired
    private MemberRepository repository;

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
        Member expected = EntityBuilder.buildMember();
        MvcResult result = super.create(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getRole().name(), getValue(result, "$.role"))
        );
    }

    @Test
    @DisplayName("update")
    public void successUpdate() throws Exception {
        Member expected = EntityBuilder.getMember(repository);
        MvcResult result = super.update(URL, expected);

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertNotNull(getValue(result, "$.updatedDate")),
                () -> assertNotNull(getValue(result, "$.updatedBy")),
                () -> assertEquals(expected.getRole().name(), getValue(result, "$.role"))
        );
    }

    @Test
    @DisplayName("readById")
    public void successReadById() throws Exception {
        Member expected = new Member();
        expected.setId(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c5bd4"));

        MvcResult result = super.readById(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertEquals("test", getValue(result, "$.createdBy")),
                () -> assertEquals(Role.ADMIN.toString(), getValue(result, "$.role"))
        );
    }

    @Test
    @DisplayName("failReadById")
    public void failReadById() throws Exception{
        MvcResult result = super.readById(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @DisplayName("delete")
    public void successDelete() throws Exception {
        Member expected = EntityBuilder.getMember(repository);

        MvcResult result = super.delete(URL, expected.getId());

        assertAll(
                () -> assertNotNull(getValue(result, "$.createdDate")),
                () -> assertNotNull(getValue(result, "$.createdBy")),
                () -> assertEquals(expected.getRole().name(), getValue(result, "$.role"))
        );
    }

    @Test
    @DisplayName("failDelete")
    public void failDelete() throws Exception {
        MvcResult result = super.delete(URL, UUID.randomUUID());
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }
}
