package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Resource extends Domain {
    private Member createdBy;
    private Member updatedBy;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate;
}
