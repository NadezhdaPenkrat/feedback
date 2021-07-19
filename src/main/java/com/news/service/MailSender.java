package com.news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// класс для рассылки сообщений
@Service // аннотация сервис, чтобы спринг создал класс автоматически
public class MailSender {
    @Autowired // проводной для JavaMailSender
    private JavaMailSender mailSender;
// определяет имя пользователя, для того чтобы возможно было указать отправителя
    @Value("${spring.mail.username}")
    private String username;

// метод, который рассылает почту, принимать на вход адресата
    public void send(String emailTo, String subject, String message) {
       //создаем простое оповещение
        SimpleMailMessage mailMessage = new SimpleMailMessage();
// заполняем оповещение
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
// после отправляется оповещение, которое сформировали
        mailSender.send(mailMessage);
    }
}
