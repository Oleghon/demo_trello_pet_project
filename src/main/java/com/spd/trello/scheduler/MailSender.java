package com.spd.trello.scheduler;

import com.spd.trello.service.MailService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
@Component
@Scope("prototype")
public class MailSender implements Runnable {
    private String cardName;
    private static AtomicInteger sendMails = new AtomicInteger();
    private final MailService mailSenderService;

    public MailSender(MailService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void run() {
        mailSenderService.send("o.cherepnin@istu.edu.ua", "remind " + this.cardName, "Reminder has been activated");
        sendMails.incrementAndGet();
        log.info("{} was sent: {}",cardName, sendMails);
    }

    public static AtomicInteger getSendMails() {
        return sendMails;
    }

    public static void setSendMails(int count) {
        sendMails.set(count);
    }
}
