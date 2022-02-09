package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Reminder;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends CommonRepository<Reminder> {
}
