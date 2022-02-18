package com.spd.domain;

import com.spd.trello.domain.enums.WorkSpaceVisibility;
import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.WorkspaceRepository;
import com.spd.trello.service.WorkSpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkSpaceTest extends BaseTest {

    @Mock
    private WorkspaceRepository repository;

    @InjectMocks
    private WorkSpaceService service;

    @BeforeEach
    public void createTestSpace() {
        workSpace.setId(UUID.fromString("1ee887d3-9065-421d-93bd-7ad5f30c3bd9"));
        workSpace.setWorkspaceMembers(Set.of(member.getId()));
    }

    @Test
    public void successCreate() {
        when(repository.save(workSpace)).thenReturn(workSpace);

        WorkSpace actual = service.create(workSpace);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(workSpace.getVisibility(), actual.getVisibility()),
                () -> assertEquals(workSpace.getCreatedBy(), actual.getCreatedBy())
        );

        verify(repository, times(1)).save(workSpace);

    }

    @Test
    public void successUpdate() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(workSpace));
        when(repository.save(workSpace)).thenReturn(workSpace);

        workSpace.setUpdatedBy("test");
        workSpace.setVisibility(WorkSpaceVisibility.PRIVATE);
        WorkSpace actual = service.update(workSpace);

        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(workSpace.getVisibility(), actual.getVisibility()),
                () -> assertEquals(workSpace.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(1, actual.getWorkspaceMembers().size())
        );

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(workSpace);
    }

    @Test
    public void successGetObjects() {
        Page<WorkSpace> expected = new PageImpl(List.of(workSpace, workSpace), pageable, 2);

        when(repository.save(workSpace)).thenReturn(workSpace);
        when(repository.findAll(pageable)).thenReturn(expected);

        List<WorkSpace> actual = service.readAll(pageable).getContent();

        assertEquals(expected.getContent().size(), actual.size());
    }

    @Test
    public void failReadById() {
        assertThrows(EntityNotFoundException.class, () -> service.readById(UUID.randomUUID()));
        verify(repository, times(1)).findById(any());
    }

    @Test
    public void successDelete() {
        when(repository.save(workSpace)).thenReturn(workSpace);
        when(repository.findById(any())).thenReturn(Optional.ofNullable(workSpace));
        doNothing().when(repository).deleteById(any());

        workSpace.setId(UUID.randomUUID());
        WorkSpace expected = service.create(BaseTest.workSpace);
        WorkSpace actual = service.delete(expected.getId());

        assertEquals(expected.getName(), actual.getName());

        verify(repository, times(1)).findById(any());
    }
}
