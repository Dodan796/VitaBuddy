package com.example.vitabuddy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class emailConfig {

    @Value("${spring.mail.username}") private String username;
    @Value("${spring.mail.password}") private String password;

    @Bean
    public JavaMailSender javaGmailSender(){
        JavaMailSenderImpl javaGmailSender = new JavaMailSenderImpl();

        javaGmailSender.setHost("smtp.gmail.com");
        javaGmailSender.setUsername(username);
        javaGmailSender.setPassword(password);
        javaGmailSender.setPort(587);
        javaGmailSender.setJavaMailProperties(getGmailProperties());

        return javaGmailSender;
    }

    private Properties getGmailProperties(){
        Properties Gproperties = new Properties();
        Gproperties.put("mail.transport.protocol", "smtp");
        Gproperties.put("mail.smtp.auth", "true");
        Gproperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Gproperties.put("mail.smtp.starttls.enable", "true");
        Gproperties.put("mail.debug", "true");
        Gproperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Gproperties.put("mail.smtp.ssl.protocols","TLSv1.3");

        return Gproperties;
    }

    // Redis 설정 Config (1.04 추가된 코드)
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }

}
