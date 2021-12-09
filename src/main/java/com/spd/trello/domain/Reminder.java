package com.spd.trello.domain;

import java.time.LocalDateTime;

public class Reminder {
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime remindOn;
    private boolean isAlive;

    public Reminder() {}

    public Reminder(LocalDateTime start, LocalDateTime end, LocalDateTime remindOn, boolean isAlive) {
        this.start = start;
        this.end = end;
        this.remindOn = remindOn;
        this.isAlive = isAlive;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public LocalDateTime getRemindOn() {
        return remindOn;
    }

    public void setRemindOn(LocalDateTime remindOn) {
        this.remindOn = remindOn;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
