package com.spd.trello.service;

import com.spd.trello.domain.resources.Reminder;
import com.spd.trello.repository_jpa.ReminderRepository;
import org.springframework.stereotype.Service;

@Service
public class ReminderService extends AbstractService<Reminder, ReminderRepository> {

    public ReminderService(ReminderRepository repository) {
        super(repository);
    }
}
