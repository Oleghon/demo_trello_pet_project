package com.spd.domain;

import com.spd.trello.domain.User;
import com.spd.trello.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest extends BaseTest {

    private static UserService service;

    public UserTest() {
        service = new UserService();
    }

    @BeforeEach
    void createTestUser() {
        user.setId(UUID.randomUUID());
        user = service.create(BaseTest.user);
    }

    @Test
    public void successCreate() {
        user = service.create("Test", "Test", "Test", "test@test.com");
        assertNotNull(user);
        assertAll(
                () -> assertNotNull(user.getCreatedDate()),
                () -> assertNotNull(user.getId()),
                () -> assertEquals("Test", user.getCreatedBy()),
                () -> assertEquals("Test", user.getFirstName()),
                () -> assertEquals("Test", user.getLastName()),
                () -> assertEquals("test@test.com", user.getEmail())
        );
    }

    @Test
    public void successGetObjects() {
        List<User> users = service.readAll();
        assertNotEquals(0, users.size());
    }

    @Test
    public void successUpdate() {
        user.setFirstName("Test2");
        user.setUpdatedBy("Test2");
        user.setCreatedBy("Test2");
        User actual = service.update(user.getId(), user);
        assertNotNull(actual);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(user.getFirstName(), actual.getFirstName()),
                () -> assertEquals(user.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertNotEquals(user.getCreatedBy(), actual.getCreatedBy())
        );
    }

    @Test
    public void failDelete() {
        assertFalse(service.delete(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        assertTrue(service.delete(user.getId()));
    }
}
