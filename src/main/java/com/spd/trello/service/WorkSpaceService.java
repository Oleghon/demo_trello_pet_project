package com.spd.trello.service;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.Member;
import com.spd.trello.domain.WorkSpace;
import com.spd.trello.domain.WorkSpaceVisibility;
import com.spd.trello.repository.Repository;
import com.spd.trello.repository.impl.WorkSpaceRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class WorkSpaceService extends AbstractService<WorkSpace> {

    private Repository<WorkSpace> repository;

    public WorkSpaceService() {
        repository = new WorkSpaceRepositoryImpl();
    }

    public WorkSpace create(String createdBy, String name, WorkSpaceVisibility visibility,
                            String desc, List<Member> members, List<Board> boards) {
        WorkSpace workSpace = new WorkSpace();
        workSpace.setCreatedBy(createdBy);
        workSpace.setName(name);
        workSpace.setDescription(desc);
        workSpace.setVisibility(visibility);
        workSpace.setWorkspaceMembers(members);
        workSpace.setBoardList(boards);
        return create(workSpace);
    }

    @Override
    public WorkSpace create(WorkSpace obj) {
        return repository.create(obj);
    }

    @Override
    public WorkSpace update(UUID id, WorkSpace obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        return repository.update(id, obj);
    }

    @Override
    public WorkSpace readById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return repository.delete(id);
    }

    @Override
    public List<WorkSpace> readAll() {
        return repository.getObjects();
    }
}
