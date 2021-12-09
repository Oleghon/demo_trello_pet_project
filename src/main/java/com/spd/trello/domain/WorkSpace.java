package com.spd.trello.domain;

import java.util.List;

public class WorkSpace {
    private String name;
    private String description;
    private List<Board> boardList;
    private List<Member> workspaceMembers;
    private WorkSpaceVisibility visibility;

}
