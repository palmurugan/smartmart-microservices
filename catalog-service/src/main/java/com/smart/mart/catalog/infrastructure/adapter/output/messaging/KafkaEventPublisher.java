package com.smart.mart.catalog.infrastructure.adapter.output.messaging;

import com.smart.mart.catalog.application.port.output.messaging.EventPublisher;
import com.smart.mart.catalog.avro.CategoryEventAvro;
import com.smart.mart.catalog.domain.event.CategoryCreatedEvent;
import com.smart.mart.catalog.domain.event.CategoryDeletedEvent;
import com.smart.mart.catalog.domain.event.CategoryUpdatedEvent;
import com.smart.mart.catalog.domain.event.DomainEvent;
import com.smart.mart.catalog.infrastructure.adapter.output.messaging.mapper.CategoryEventAvroMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, CategoryEventAvro> kafkaTemplate;
    private final CategoryEventAvroMapper avroMapper;

    @Value("${kafka.topic.category-events}")
    private String topicName;

    @Override
    public void publish(DomainEvent event) {
        try {
            CategoryEventAvro avroEvent = avroMapper.toAvro(event);
            String key = extractKey(event);

            CompletableFuture<SendResult<String, CategoryEventAvro>> future =
                    kafkaTemplate.send(topicName, key, avroEvent);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Event published successfully: eventId={}, topic={}, partition={}, offset={}",
                            event.getEventId(),
                            topicName,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish event: eventId={}, topic={}",
                            event.getEventId(), topicName, ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing event to Kafka", e);
            throw new RuntimeException("Failed to publish event", e);
        }
    }

    private String extractKey(DomainEvent event) {
        // Use category ID as partition key for ordering
        if (event instanceof CategoryCreatedEvent) {
            return ((CategoryCreatedEvent) event)
                    .getCategoryId().toString();
        } else if (event instanceof CategoryUpdatedEvent) {
            return ((CategoryUpdatedEvent) event)
                    .getCategoryId().toString();
        } else if (event instanceof CategoryDeletedEvent) {
            return ((CategoryDeletedEvent) event)
                    .getCategoryId().toString();
        }
        return event.getEventId().toString();
    }
}
