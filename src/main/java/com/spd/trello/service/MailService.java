package com.spd.trello.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cherepnin11@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        log.info("Send mail to - {}", toEmail);
        mailSender.send(message);
    }

}


