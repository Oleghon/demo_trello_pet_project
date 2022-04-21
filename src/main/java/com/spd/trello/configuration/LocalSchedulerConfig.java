package com.spd.trello.configuration;

import com.spd.trello.domain.items.Reminder;
import com.spd.trello.repository_jpa.ReminderRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class LocalSchedulerConfig {

    private final ReminderRepository repository;

    public LocalSchedulerConfig(ReminderRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60000)
    public void RemindMessage() {
        List<Reminder> reminders = repository.findAllByRemindOnBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
        for (Reminder reminder : reminders) {
            System.out.println("Reminder: " + reminder.getId() + "has been activated");
        }
    }
}
