package com.example.vitabuddy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class emailConfig {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaGmailSender() {
        JavaMailSenderImpl javaGmailSender = new JavaMailSenderImpl();
        javaGmailSender.setHost("smtp.gmail.com");
        javaGmailSender.setUsername(username);
        javaGmailSender.setPassword(password);
        javaGmailSender.setPort(587);
        javaGmailSender.setJavaMailProperties(getGmailProperties());
        return javaGmailSender;
    }

    private Properties getGmailProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.3");
        return props;
    }
}
