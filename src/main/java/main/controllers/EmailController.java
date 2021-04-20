package main.controllers;

import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class EmailController {
    private final JavaMailSender mailSender;

    @Autowired
    AuthService authService;

    @Autowired
    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RequestMapping(value = "/send")
    public String send() {
//        authService.
        return LocalDateTime.now().toString();
    }
}