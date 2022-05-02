package com.spd.trello.configuration;

import com.spd.trello.service.MailService;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class MailSender implements Runnable {
    private String email;
    private static AtomicInteger sendMails = new AtomicInteger();
    private MailService mailSenderService;

    public MailSender(MailService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void run() {
        mailSenderService.send("o.cherepnin@istu.edu.ua", "remind " + email, "Reminder has been activated");
        sendMails.incrementAndGet();
    }

    public static AtomicInteger getSendMails() {
        return sendMails;
    }

    public static void setSendMails(int count) {
        MailSender.sendMails.set(count);
    }
}
