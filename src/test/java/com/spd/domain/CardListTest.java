package com.spd.domain;

import com.spd.trello.domain.resources.CardList;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.CardListRepository;
import com.spd.trello.service.CardListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardListTest extends BaseTest {
    @Mock
    private CardListRepository repository;

    @InjectMocks
    private CardListService service;

    @BeforeEach
    public void createTestBoard() {
        cardList.setId(UUID.fromString("7ee597d3-9365-421d-96bd-7ad5f30c3bd9"));
    }

    @Test
    public void successCreate() {
        when(repository.save(cardList)).thenReturn(cardList);

        CardList actual = service.create(cardList);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(cardList.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(cardList.getArchived(), actual.getArchived()),
                () -> assertEquals(cardList.getName(), actual.getName())
        );

        verify(repository, times(1)).save(cardList);
    }

    @Test
    public void successUpdate() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(cardList));
        when(repository.save(cardList)).thenReturn(cardList);

        cardList.setUpdatedBy("test");
        cardList.setName("new name");
        cardList.setArchived(true);
        CardList actual = service.update(cardList);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(cardList.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(cardList.getName(), actual.getName()),
                () -> assertTrue(cardList.getArchived())
        );

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(cardList);
    }

    @Test
    public void successGetObjects() {
        Page<CardList> expected = new PageImpl(List.of(cardList, cardList), pageable, 2);

        when(repository.save(cardList)).thenReturn(cardList);
        when(repository.findAll(pageable)).thenReturn(expected);

        List<CardList> actual = service.readAll(pageable).getContent();

        assertEquals(expected.getContent().size(), actual.size());
    }

    @Test
    public void failDelete() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(UUID.randomUUID()));
        verify(repository, times(1)).findById(any());
    }

    @Test
    public void successDelete() {
        when(repository.save(cardList)).thenReturn(cardList);
        when(repository.findById(any())).thenReturn(Optional.ofNullable(cardList));
        doNothing().when(repository).deleteById(any());

        workSpace.setId(UUID.randomUUID());
        CardList expected = service.create(cardList);
        CardList actual = service.delete(expected.getId());

        assertEquals(expected.getName(), actual.getName());

        verify(repository, times(1)).save(cardList);
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).deleteById(any());
    }
}
