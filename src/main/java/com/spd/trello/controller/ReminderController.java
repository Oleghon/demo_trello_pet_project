package com.spd.trello.controller;

import com.spd.trello.domain.resources.Reminder;
import com.spd.trello.service.ReminderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reminders")
public class ReminderController extends AbstractController<Reminder, ReminderService>{
    public ReminderController(ReminderService service) {
        super(service);
    }
}
