package com.spd.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Comment {
    private Member member;
    private String text;
    private LocalDateTime date;
    private List<Attachment> attachments;

    public Comment() {}

    public Comment(Member member, String text, LocalDateTime date) {
        this.member = member;
        this.text = text;
        this.date = date;
    }

    public Comment(Member member, String text, LocalDateTime date, List<Attachment> attachments) {
        this.member = member;
        this.text = text;
        this.date = date;
        this.attachments = attachments;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
