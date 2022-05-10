package com.spd.trello.domain.items;

import com.spd.trello.domain.Domain;
import com.spd.trello.domain.resources.Card;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "attachments")
@Entity
public class Attachment extends Domain {
    private String context;
    private String name;
    @Lob
    private byte[] file;
    private UUID keyId;
}
