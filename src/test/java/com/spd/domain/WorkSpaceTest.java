package com.spd.domain;

import com.spd.trello.domain.WorkSpace;
import com.spd.trello.domain.WorkSpaceVisibility;
import com.spd.trello.service.WorkSpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WorkSpaceTest extends BaseTest{

    private static WorkSpaceService workSpaceService;

    public WorkSpaceTest() {
        workSpaceService = new WorkSpaceService();
        }

    @BeforeEach
    public void createTestSpace() {
        workSpace.setId(UUID.randomUUID());
        workSpace = workSpaceService.create(workSpace);
    }

    @Test
    public void successCreate() {
        workSpace.setId(UUID.randomUUID());
        WorkSpace actual = workSpaceService.create(workSpace);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(workSpace.getVisibility(), actual.getVisibility()),
                () -> assertEquals(workSpace.getCreatedBy(), actual.getCreatedBy())
        );
    }

    @Test
    public void successUpdate() {
        workSpace.setUpdatedBy("test");
        workSpace.setVisibility(WorkSpaceVisibility.PRIVATE);
        WorkSpace actual = workSpaceService.update(workSpace.getId(), workSpace);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(workSpace.getVisibility(), actual.getVisibility()),
                () -> assertEquals(workSpace.getUpdatedBy(), actual.getUpdatedBy()),
                () -> assertEquals(1, actual.getWorkspaceMembers().size())
        );
    }

    @Test
    public void successGetObjects() {
            List<WorkSpace> workSpaces = workSpaceService.readAll();
        assertNotEquals(0, workSpaces.size());
    }

    @Test
    public void failDelete() {
        assertFalse(workSpaceService.delete(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        assertTrue(workSpaceService.delete(workSpace.getId()));
    }
}
