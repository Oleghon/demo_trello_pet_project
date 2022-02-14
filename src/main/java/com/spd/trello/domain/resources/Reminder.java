package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "reminders")
@Entity
public class Reminder extends Resource {

    @Column(name = "starts")
    private LocalDateTime start;
    @Column(name = "ends")
    private LocalDateTime end;
    private LocalDateTime remindOn;
    private Boolean alive;

}

