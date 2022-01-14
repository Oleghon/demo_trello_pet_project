package com.spd.trello.service;

import com.spd.trello.domain.CheckList;
import com.spd.trello.repository.impl.CheckListRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CheckListService extends AbstractService<CheckList> {
    private CheckListRepositoryImpl checkListRepository;

    public CheckListService() {
        this.checkListRepository = new CheckListRepositoryImpl();
    }

    @Override
    public CheckList create(CheckList obj) {
        return checkListRepository.create(obj);
    }

    @Override
    public CheckList update(UUID id, CheckList obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        return checkListRepository.update(id, obj);
    }

    @Override
    public CheckList readById(UUID id) {
        return checkListRepository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return checkListRepository.delete(id);
    }

    @Override
    public List<CheckList> readAll() {
        return checkListRepository.getObjects();
    }
}
