package com.securevault.event.notification.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("{notifications.exchange}")
    private String exchangeName;

    @Value("{notifications.routing.key}")
    private String routingKey;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(Object notificationMessage) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, notificationMessage);
    }
}
