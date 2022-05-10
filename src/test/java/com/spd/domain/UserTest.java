package com.spd.domain;

import com.spd.trello.domain.resources.User;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.UserRepository;
import com.spd.trello.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest extends BaseTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @BeforeEach
    private void setUp() {
        user.setId(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c3bd9"));
    }

    @Test
    public void successCreate() {
        when(repository.save(user)).thenReturn(user);
        User actual = service.create(user);
        assertThat(actual.getCreatedDate()).isNotNull();
        assertAll(
                () -> assertEquals(user.getFirstName(), actual.getFirstName()),
                () -> assertEquals(user.getLastName(), actual.getLastName()),
                () -> assertEquals(user.getEmail(), actual.getEmail())
        );
        verify(repository, times(1)).save(user);
    }

    @Test
    public void successGetObjects() {
        user.setId(UUID.randomUUID());
        service.create(user);
        Page<User> expected = new PageImpl<User>(List.of(user, user), pageable, 2);

        when(repository.findAll(pageable)).thenReturn(expected);

        List<User> actual = service.readAll(pageable).getContent();
        assertEquals(expected.getContent().size(), actual.size());

    }

    @Test
    public void successUpdate() {
        when(repository.findById(any())).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        user.setFirstName("Test2");
        user.setUpdatedBy("Test2");
        user.setCreatedBy("Test2");

        User actual = service.update(user);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(user.getFirstName(), actual.getFirstName()),
                () -> assertEquals(user.getUpdatedBy(), actual.getUpdatedBy())
        );

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(user);
    }

    @Test
    public void failReadById() {
        assertThrows(EntityNotFoundException.class, () -> service.readById(UUID.randomUUID()));
        verify(repository, times(1)).findById(any());
    }

    @Test
    public void successDelete() {
        when(repository.save(user)).thenReturn(user);
        when(repository.findById(any())).thenReturn(Optional.of(user));
        doNothing().when(repository).deleteById(any());

        user.setId(UUID.randomUUID());
        User expected = service.create(user);
        User actual = service.delete(expected.getId());

        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());

        verify(repository, times(1)).save(user);
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).deleteById(any());
    }
}
