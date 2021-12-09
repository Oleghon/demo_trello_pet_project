package com.spd.trello.domain;

import lombok.Data;
import java.util.List;

@Data
public class WorkSpace {
    private String name;
    private String description;
    private List<Board> boardList;
    private List<Member> workspaceMembers;
    private WorkSpaceVisibility visibility;
}
