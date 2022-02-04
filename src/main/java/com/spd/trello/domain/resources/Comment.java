package com.spd.trello.domain.resources;

import com.spd.trello.domain.items.Attachment;
import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends Resource {
    private String text;
    @EqualsAndHashCode.Exclude
    private Card card;
    private List<Attachment> attachments;
}
