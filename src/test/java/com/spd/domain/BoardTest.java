package com.spd.domain;

import com.spd.trello.domain.enums.BoardVisibility;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.BoardRepository;
import com.spd.trello.service.BoardService;
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

public class BoardTest extends BaseTest {

    @Mock
    private BoardRepository repository;

    @InjectMocks
    private BoardService service;

    @BeforeEach
    public void createTestBoard() {
        board.setId(UUID.fromString("7ee897d3-9045-521d-93bd-7ad5f30c3bd9"));
        board.setMembers(List.of(member.getId()));
    }

    @Test
    public void successCreate() {
        when(repository.save(board)).thenReturn(board);

        Board actual = service.create(board);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(board.getVisibility(), actual.getVisibility()),
                () -> assertEquals(board.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(board.getArchived(), actual.getArchived()),
                () -> assertEquals(board.getName(), actual.getName()),
                () -> assertEquals(board.getDescription(), actual.getDescription()),
                () -> assertEquals(1, actual.getMembers().size())
        );

        verify(repository, times(1)).save(board);
    }

    @Test
    public void successFindById() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(board));

        Board actual = service.readById(board.getId());
        assertAll(
                () -> assertEquals(board.getVisibility(), actual.getVisibility()),
                () -> assertEquals(board.getCreatedBy(), actual.getCreatedBy()),
                () -> assertEquals(board.getArchived(), actual.getArchived()),
                () -> assertEquals(board.getName(), actual.getName()),
                () -> assertEquals(board.getDescription(), actual.getDescription())
        );

        verify(repository, times(1)).findById(any());
    }

    @Test
    public void successUpdate() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(board));
        when(repository.save(board)).thenReturn(board);

        board.setUpdatedBy("test");
        board.setVisibility(BoardVisibility.PRIVATE);
        board.setDescription("test desc update");
        Board actual = service.update(board);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(board.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(board.getVisibility(), actual.getVisibility()),
                () -> assertEquals(board.getDescription(), actual.getDescription())
        );
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(board);

    }

    @Test
    public void successGetObjects() {
        Page<Board> expected = new PageImpl(List.of(board, board), pageable, 2);

        when(repository.save(board)).thenReturn(board);
        when(repository.findAll(pageable)).thenReturn(expected);

        List<Board> actual = service.readAll(pageable).getContent();

        assertEquals(expected.getContent().size(), actual.size());
    }

    @Test
    public void failReadById() {
        assertThrows(EntityNotFoundException.class, () -> service.readById(UUID.randomUUID()));
        verify(repository, times(1)).findById(any());
    }

    @Test
    public void successDelete() {
        when(repository.save(board)).thenReturn(board);
        when(repository.findById(any())).thenReturn(Optional.ofNullable(board));
        doNothing().when(repository).deleteById(any());

        workSpace.setId(UUID.randomUUID());
        Board expected = service.create(board);
        Board actual = service.delete(expected.getId());

        assertEquals(expected.getName(), actual.getName());

        verify(repository, times(1)).save(board);
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).deleteById(any());
    }
}
