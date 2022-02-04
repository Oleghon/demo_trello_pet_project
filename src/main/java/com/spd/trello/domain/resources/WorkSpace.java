package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import com.spd.trello.domain.enums.WorkSpaceVisibility;
import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Member;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkSpace extends Resource {
    private String name;
    private String description;
    private List<Board> boardList = new ArrayList<>();
    private List<Member> workspaceMembers = new ArrayList<>();
    private WorkSpaceVisibility visibility = WorkSpaceVisibility.PUBLIC;
}
