package com.spd.trello.domain.items;

import com.spd.trello.domain.Domain;
import lombok.Data;

@Data
public class AttachmentResponse extends Domain {

    private String name;
    private String link;
    private String context;
}
