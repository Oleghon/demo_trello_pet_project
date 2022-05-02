package com.spd.trello.controller;

import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.service.WorkSpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/workspaces")
public class WorkspaceController extends AbstractController<WorkSpace, WorkSpaceService> {

    public WorkspaceController(WorkSpaceService service) {
        super(service);
    }

    @PostMapping("/{id}/add_member")
    public ResponseEntity<WorkSpace> addMember(@PathVariable UUID id, @RequestBody Member member){
        WorkSpace workSpace = service.addMember(id, member);
        log.info("entity with id: {} has just been added to workspace: {}", member.getId(), workSpace.getId());
        return new ResponseEntity<>(workSpace, HttpStatus.OK);
    }
}
