package com.example.demo.service;

import com.example.demo.entity.OutboxEvent;
import com.example.demo.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OutboxPublisherService {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisherService.class);

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxPublisherService(OutboxEventRepository outboxEventRepository, RabbitTemplate rabbitTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public void publishEvent(String aggregateType, String aggregateId, String eventType, String payload) {
        String eventId = UUID.randomUUID().toString();
        OutboxEvent event = new OutboxEvent(eventId, aggregateType, aggregateId, eventType, payload);
        outboxEventRepository.save(event);
        log.info("Persisted transactional outbox event: id={}, eventType={}", eventId, eventType);
    }

    @Scheduled(fixedDelay = 5000)
    public void processPendingOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc("PENDING");
        if (pendingEvents.isEmpty()) return;

        log.info("Processing {} pending outbox events for RabbitMQ dispatch...", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                // Publish to RabbitMQ exchange
                rabbitTemplate.convertAndSend("hackforgeExchange", event.getEventType(), event.getPayload());
                event.setStatus("PROCESSED");
                event.setProcessedAt(LocalDateTime.now());
                outboxEventRepository.save(event);
                log.info("Successfully published outbox event {} to RabbitMQ", event.getId());
            } catch (Exception e) {
                log.warn("Could not dispatch outbox event {} to RabbitMQ broker (broker down or offline): {}", event.getId(), e.getMessage());
            }
        }
    }
}
