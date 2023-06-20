package com.example.demowithtests.service.sender;

import org.springframework.mail.MailException;

public interface EmailService {
    void sendEmail(String toMail, String subject, String body) throws MailException;
}
