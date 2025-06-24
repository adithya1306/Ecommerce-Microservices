package com.adi.order_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendConfirmationMail(String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setBcc("adibala1306@gmail.com");
        message.setSubject("Order Confirmation " + new Date());
        message.setText(content);
        javaMailSender.send(message);
    }
}
