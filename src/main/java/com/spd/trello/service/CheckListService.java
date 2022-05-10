package com.spd.trello.service;

import com.spd.trello.domain.resources.CheckList;
import com.spd.trello.repository_jpa.CheckListRepository;
import org.springframework.stereotype.Service;

@Service
public class CheckListService extends AbstractService<CheckList, CheckListRepository> {

    public CheckListService(CheckListRepository repository) {
        super(repository);
    }

}
