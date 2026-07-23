package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_QUEUE = "hackforge.notifications.queue";
    public static final String NOTIFICATION_DLQ = "hackforge.notifications.dlq";
    public static final String HACKFORGE_EXCHANGE = "hackforge.direct.exchange";
    public static final String HACKFORGE_DLX = "hackforge.dlx.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.routing.key";
    public static final String NOTIFICATION_DLK = "notification.dlk";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(HACKFORGE_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(HACKFORGE_DLX);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE)
                .withArgument("x-dead-letter-exchange", HACKFORGE_DLX)
                .withArgument("x-dead-letter-routing-key", NOTIFICATION_DLK)
                .build();
    }

    @Bean
    public Queue notificationDeadLetterQueue() {
        return QueueBuilder.durable(NOTIFICATION_DLQ).build();
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue).to(exchange).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding(Queue notificationDeadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(notificationDeadLetterQueue).to(deadLetterExchange).with(NOTIFICATION_DLK);
    }
}
