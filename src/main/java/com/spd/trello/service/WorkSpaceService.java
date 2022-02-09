package com.spd.trello.service;

import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.repository_jpa.WorkspaceRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkSpaceService extends AbstractService<WorkSpace, WorkspaceRepository> {
    public WorkSpaceService(WorkspaceRepository repository) {
        super(repository);
    }
}
