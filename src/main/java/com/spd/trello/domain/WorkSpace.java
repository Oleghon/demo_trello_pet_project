package com.spd.trello.domain;

import java.util.List;

public class WorkSpace {
    private String name;
    private String description;
    private List<Board> boardList;
    private List<Member> workspaceMembers;
    private WorkSpaceVisibility visibility;

    public WorkSpace(String name, String description, List<Board> boardList,
                     List<Member> workspaceMembers, WorkSpaceVisibility visibility) {
        this.name = name;
        this.description = description;
        this.boardList = boardList;
        this.workspaceMembers = workspaceMembers;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Board> getBoardList() {
        return boardList;
    }

    public void setBoardList(List<Board> boardList) {
        this.boardList = boardList;
    }

    public List<Member> getWorkspaceMembers() {
        return workspaceMembers;
    }

    public void setWorkspaceMembers(List<Member> workspaceMembers) {
        this.workspaceMembers = workspaceMembers;
    }

    public WorkSpaceVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(WorkSpaceVisibility visibility) {
        this.visibility = visibility;
    }
}
