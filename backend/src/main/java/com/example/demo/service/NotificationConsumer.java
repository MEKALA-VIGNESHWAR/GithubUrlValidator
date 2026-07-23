package com.example.demo.service;

import com.example.demo.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void processNotification(Map<String, Object> notificationPayload) {
        try {
            logger.info("Processing background notification message: {}", notificationPayload);
            String recipient = (String) notificationPayload.get("recipient");
            String message = (String) notificationPayload.get("message");
            // Perform async email or push notification dispatch logic
            logger.info("Successfully dispatched notification to {}", recipient);
        } catch (Exception e) {
            logger.error("Failed to process notification message, forwarding to DLQ: ", e);
            throw e; // Re-throw to trigger DLQ retry policy
        }
    }
}
