package com.spd.trello.service;

import com.spd.trello.domain.resources.CheckList;
import com.spd.trello.repository.impl.CheckListRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CheckListService extends AbstractService<CheckList> {
    private CheckListRepositoryImpl checkListRepository;

    @Autowired
    public CheckListService(CheckListRepositoryImpl checkListRepository) {
        this.checkListRepository = checkListRepository;
    }

    @Override
    public CheckList create(CheckList obj) {
        checkListRepository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public CheckList update(UUID id, CheckList obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        checkListRepository.update(id, obj);
        return readById(id);
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
