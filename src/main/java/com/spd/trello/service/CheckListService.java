package com.spd.trello.service;

import com.spd.trello.domain.resources.CheckList;
import com.spd.trello.repository_jpa.CheckListRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckListService extends AbstractService<CheckList, CheckListRepository> {

    public CheckListService(CheckListRepository repository) {
        super(repository);
    }

    @Override
    public CheckList create(CheckList entity) {
        entity.getItems().forEach(item -> item.setCheckList(entity));
        return super.create(entity);
    }

    @Override
    public CheckList update(CheckList entity) {
        entity.getItems().forEach(item -> item.setCheckList(entity));
        return super.update(entity);
    }
}
