package com.spd.trello.scheduler;

import com.spd.trello.domain.items.Reminder;
import com.spd.trello.domain.resources.Card;
import com.spd.trello.repository_jpa.CardRepository;
import com.spd.trello.repository_jpa.ReminderRepository;
import com.spd.trello.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class LocalScheduler {

    private final ReminderRepository repository;
    private final CardRepository cardRepository;
    private final MailService mailService;
    private final ThreadPoolTaskExecutor executorService;
    @Autowired(required = false)
    private MailSender mailSender;

    @Autowired
    public LocalScheduler(ReminderRepository repository, CardRepository cardRepository, MailService mailSender,
                          @Qualifier("threadPoolTaskExecutor") ThreadPoolTaskExecutor executorService) {
        this.repository = repository;
        this.cardRepository = cardRepository;
        this.mailService = mailSender;
        this.executorService = executorService;
    }

    @Scheduled(fixedRate = 60000)
    public void runReminder() {
        MailSender.setSendMails(0);
        List<Reminder> activeReminders =
                repository.findAllByRemindOnBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
        log.info("reminders activated: " + activeReminders.size());

        activeReminders.forEach(reminder -> {
            Card card = cardRepository.findCardByReminderId(reminder.getId());
            mailSender = new MailSender(mailService);
            mailSender.setCardName(card.getName());
            executorService.execute(mailSender);
        });
    }
}
