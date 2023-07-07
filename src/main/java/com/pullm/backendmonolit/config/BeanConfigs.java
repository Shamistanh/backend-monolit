package com.pullm.backendmonolit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class BeanConfigs {

    @Bean
    public JavaMailSenderImpl getJavaMailSenderImpl() {
        return new JavaMailSenderImpl();
    }
}
