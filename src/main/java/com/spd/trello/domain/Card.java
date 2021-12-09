package com.spd.trello.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Card {
    private String name;
    private String description;
    private boolean isArchived;
    private LocalDateTime creationDate;
    private List<Member> assignedMembers;
    private List<Comment> comments;
    private List<Label> labels;
    private Reminder reminder;
    private List<CheckList> checkList;

    public Card() {}

    public Card(String name, String description, boolean isArchived,
                LocalDateTime creationDate, List<Member> assignedMembers) {
        this.name = name;
        this.description = description;
        this.isArchived = isArchived;
        this.creationDate = creationDate;
        this.assignedMembers = assignedMembers;
    }

    public Card(String name, String description, boolean isArchived, LocalDateTime creationDate,
                List<Member> assignedMembers, List<Comment> comments, List<Label> labels,
                Reminder reminder, List<CheckList> checkList) {
        this.name = name;
        this.description = description;
        this.isArchived = isArchived;
        this.creationDate = creationDate;
        this.assignedMembers = assignedMembers;
        this.comments = comments;
        this.labels = labels;
        this.reminder = reminder;
        this.checkList = checkList;
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

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Member> getAssignedMembers() {
        return assignedMembers;
    }

    public void setAssignedMembers(List<Member> assignedMembers) {
        this.assignedMembers = assignedMembers;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }

    public List<CheckList> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckList> checkList) {
        this.checkList = checkList;
    }
}
