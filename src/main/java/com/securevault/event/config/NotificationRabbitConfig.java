package com.securevault.event.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty( name = "rabbitmq.enabled", havingValue = "true")
public class NotificationRabbitConfig {

    @Value("${notifications.exchange}")
    private String exchangeName;

    @Value("${notifications.queue}")
    private String queueName;

    @Value("${notifications.routing.key}")
    private String routingKey;

    @Bean
    public Queue notificationQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return org.springframework.amqp.core.BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(routingKey);
    }

}
