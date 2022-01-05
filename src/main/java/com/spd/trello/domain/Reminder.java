package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Reminder extends Resource{
    private Card card;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime remindOn;
    private Boolean alive;

}
