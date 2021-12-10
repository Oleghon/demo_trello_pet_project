package com.spd.trello.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkSpace {
    private String name;
    private String description;
    private List<Board> boardList = new ArrayList<>();
    private List<Member> workspaceMembers = new ArrayList<>();
    private WorkSpaceVisibility visibility = WorkSpaceVisibility.PUBLIC;
}
