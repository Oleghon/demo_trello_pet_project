package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends Resource{
    private Member member;
    private String text;
    private LocalDateTime date = LocalDateTime.now();
    private List<Attachment> attachments;
}
