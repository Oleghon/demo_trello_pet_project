package com.spd.trello.controller;

import com.spd.trello.domain.resources.CheckList;
import com.spd.trello.service.CheckListService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checklists")
public class CheckListController extends AbstractController<CheckList, CheckListService>{

    public CheckListController(CheckListService service) {
        super(service);
    }
}
