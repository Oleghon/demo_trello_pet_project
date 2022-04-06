package com.spd.trello.domain.items;

import com.spd.trello.annotation.RightReminder;
import com.spd.trello.domain.Domain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "reminders")
@Entity
@RightReminder
public class Reminder extends Domain {

    @Column(name = "starts")
    @NotNull(message = "Field \"start\" cannot be null")
    @FutureOrPresent
    private LocalDateTime start;
    @Column(name = "ends")
    @Future
    private LocalDateTime end;
    @Future
    private LocalDateTime remindOn;
    private Boolean alive;

}

