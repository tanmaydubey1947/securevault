package com.securevault.event.notification;

import com.securevault.model.dto.notification.NotificationRequest;
import com.securevault.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty( name = "rabbitmq.enabled", havingValue = "true")
public class NotificationConsumer {

    @Autowired private NotificationService notificationService;

    @RabbitListener(queues = "${notifications.queue}")
    public void processNotification(NotificationRequest notificationMessage) {
        log.info("Processing notification message: {}", notificationMessage);
        notificationService.sendNotification(notificationMessage);
    }
}
