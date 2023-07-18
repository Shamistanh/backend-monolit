package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.services.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Log4j2
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    public static final String EMAIL_TEMPLATE = "emailtemplate";
    public static final String UTF_8_ENCODING = "UTF-8";
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void send(User user, String otp) {

        Context context = new Context();
        context.setVariable("name", user.getFullName().split(" ")[0]);
        context.setVariable("otp", otp);

        String text = templateEngine.process(EMAIL_TEMPLATE, context);

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(subject);
            helper.setFrom("ucon.office@gmail.com");
            helper.setTo(user.getEmail());
            helper.setText(text, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            log.error(e);
            throw new RuntimeException(e.getMessage());
        }


    }
}
