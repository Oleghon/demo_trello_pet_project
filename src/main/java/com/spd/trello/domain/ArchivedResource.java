package com.spd.trello.domain;

import com.spd.trello.domain.Resource;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class ArchivedResource extends Resource {
    private Boolean archived = false;
}
