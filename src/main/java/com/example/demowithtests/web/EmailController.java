package com.example.demowithtests.web;

import com.example.demowithtests.service.sender.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
@Slf4j
@Tag(name = "Email", description = "Email API")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public Optional<String> sendMessage(String to, String subject, String body) {
        emailService.sendEmail(to, subject, body);
        return Optional.of("Message send");
    }
}
