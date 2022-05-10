package com.spd.domain;

import com.spd.trello.domain.resources.Card;
import com.spd.trello.domain.items.Reminder;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.CardRepository;
import com.spd.trello.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardTest extends BaseTest {

    @Mock
    private CardRepository repository;

    @InjectMocks
    private CardService service;

    @BeforeEach
    public void createTestCard() {
        card.setId(UUID.fromString("7ee897d3-9061-421d-03bd-7ad5f30c7bd9"));
    }

    @Test
    public void successCreate() {
        when(repository.save(card)).thenReturn(card);

        Card actual = service.create(card);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertEquals(card.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(card.getArchived(), actual.getArchived()),
                () -> assertEquals(card.getAssignedMembers().size(), actual.getAssignedMembers().size()),
                () -> assertEquals(card.getName(), actual.getName())
        );

        verify(repository, times(1)).save(card);
    }

    @Test
    public void successUpdate() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(card));
        when(repository.save(card)).thenReturn(card);

        card.setUpdatedBy("test");
        card.setName("new name");
        card.setDescription("new desc");
        Card actual = service.update(card);

        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(card.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(card.getName(), actual.getName())
        );

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(card);
    }

    @Test
    public void addReminder() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(card));
        when(repository.save(card)).thenReturn(card);

        Reminder reminder = new Reminder();
        reminder.setAlive(true);
        reminder.setStart(LocalDateTime.now());
        card.setReminder(reminder);
        Card actual = service.update(card);

        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertNotNull(actual.getReminder()),
                () -> assertNotNull(actual.getReminder().getStart()),
                () -> assertEquals(card.getReminder().getAlive(), actual.getReminder().getAlive())
        );

        card.setReminder(null);
        Card actual2 = service.update(card);
        assertNull(actual2.getReminder());

        verify(repository, times(2)).findById(any());
        verify(repository, times(2)).save(card);
    }

    @Test
    public void successGetObjects() {
        Page<Card> expected = new PageImpl(List.of(card, card), pageable, 2);

        when(repository.save(card)).thenReturn(card);
        when(repository.findAll(pageable)).thenReturn(expected);

        List<Card> actual = service.readAll(pageable).getContent();

        assertEquals(expected.getContent().size(), actual.size());
    }

    @Test
    public void failReadById() {
        assertThrows(EntityNotFoundException.class, () -> service.readById(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        when(repository.save(card)).thenReturn(card);
        when(repository.findById(any())).thenReturn(Optional.ofNullable(card));
        doNothing().when(repository).deleteById(any());

        workSpace.setId(UUID.randomUUID());
        Card expected = service.create(card);
        Card actual = service.delete(expected.getId());

        assertEquals(expected.getName(), actual.getName());

        verify(repository, times(1)).save(card);
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).deleteById(any());
    }
}
