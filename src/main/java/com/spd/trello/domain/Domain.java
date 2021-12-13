package com.spd.trello.domain;

import lombok.Data;
import java.util.UUID;

@Data
public class Domain {
   protected UUID id = UUID.randomUUID();
}
