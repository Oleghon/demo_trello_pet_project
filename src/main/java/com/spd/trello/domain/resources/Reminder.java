package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "reminders")
@Entity
public class Reminder extends Resource {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;
    @Column(name = "starts")
    private Timestamp start;
    @Column(name = "ends")
    private Timestamp end;
    private Timestamp remindOn;
    private Boolean alive;

}

