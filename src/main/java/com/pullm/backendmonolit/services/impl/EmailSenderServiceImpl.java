package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;

    @Override
    public void send(User user, String otp) {

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        String content = """
                Hello %s
                For security reason, you're required to use the following One Time Password to login: %s
               
                Note: This OTP is set to expire in 5 minutes.
                """.formatted(user.getUsername(), otp);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("ucon.office@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
}
