package com.news.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// класс конфигурации, в котором генерируем JavaMailSenderImpl
@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;

    //  метод получения отправителя уведомлений
    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // устанавливаем host
        mailSender.setHost(host);
        // устанавливаем port
        mailSender.setPort(port);
        // устанавливаем имя пользователя
        mailSender.setUsername(username);
        // устанавливаем пароль
        mailSender.setPassword(password);

        // устанавливаем свойсва
        Properties properties = mailSender.getJavaMailProperties();

        // прописываем предопределённые имена свойств : протокол и дэбаг,
        //для получения уведомления  о том, если что -то не так
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", debug);

        return mailSender;
    }
}
