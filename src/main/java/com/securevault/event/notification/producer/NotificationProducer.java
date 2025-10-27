package com.securevault.event.notification.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${notifications.exchange}")
    private String exchangeName;

    @Value("${notifications.routing.key}")
    private String routingKey;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void processNotification(final Object notificationMessage) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, notificationMessage);
        } catch (final Exception e) {
            log.error("Failed to send notification message to RabbitMQ: {}", e.getMessage());
        }
    }
}
