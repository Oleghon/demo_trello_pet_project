package com.spd.trello.repository_jpa;

import com.spd.trello.domain.items.Reminder;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends CommonRepository<Reminder> {
    @Query("select r from Reminder r where r.remindOn between ?1 and ?2")
    List<Reminder> findAllByRemindOnBetween(LocalDateTime before, LocalDateTime after);
}
