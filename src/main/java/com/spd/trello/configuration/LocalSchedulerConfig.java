package com.spd.trello.configuration;

import com.spd.trello.domain.items.Reminder;
import com.spd.trello.repository_jpa.ReminderRepository;
import com.spd.trello.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
@EnableScheduling
public class LocalSchedulerConfig {

    private final ReminderRepository repository;
    private final MailService mailService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    public LocalSchedulerConfig(ReminderRepository repository, MailService mailSender) {
        this.repository = repository;
        this.mailService = mailSender;
    }

    @Scheduled(fixedRate = 60000)
    @EventListener(ApplicationReadyEvent.class)
    public void runReminder() {
        List<Reminder> activeReminders =
                repository.findAllByRemindOnBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
        log.info("reminders activated: " + activeReminders.size());
        MailSender mailSender = new MailSender(mailService);
        activeReminders.forEach(reminder -> {
            mailSender.setEmail(reminder.getId().toString());
            executorService.submit(mailSender);
        });

//        executorService.shutdown();

        log.debug("The mail has been sent: " + MailSender.getSendMails());
        MailSender.setSendMails(0);
    }
}
