package com.securevault.service.notification;

import com.securevault.model.dto.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired private JavaMailSender mailSender;

    @Value("${email.sender.from}")
    private String fromEmail;

    @Override
    public void sendNotification(NotificationRequest request) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getRecipient());
        message.setSubject(request.getSubject());
        message.setText(request.getMessage());
        message.setFrom(fromEmail);

        log.info("Sending email to: {}, Subject: {}, Message: {}",
                request.getRecipient(), request.getSubject(), request.getMessage());
        mailSender.send(message);
    }
}
