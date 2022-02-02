package com.spd.trello.controller;

import com.spd.trello.domain.WorkSpace;
import com.spd.trello.service.WorkSpaceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController extends AbstractController<WorkSpace, WorkSpaceService> {

    public WorkspaceController(WorkSpaceService service) {
        super(service);
    }
}
