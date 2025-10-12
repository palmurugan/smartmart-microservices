package com.smart.mart.catalog.infrastructure.adapter.output.messaging.mapper;

import com.smart.mart.catalog.avro.CategoryEventAvro;
import com.smart.mart.catalog.domain.event.CategoryCreatedEvent;
import com.smart.mart.catalog.domain.event.CategoryDeletedEvent;
import com.smart.mart.catalog.domain.event.CategoryUpdatedEvent;
import com.smart.mart.catalog.domain.event.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryEventAvroMapper {

    public CategoryEventAvro toAvro(DomainEvent event) {
        if (event instanceof CategoryCreatedEvent) {
            return toAvro((CategoryCreatedEvent) event);
        } else if (event instanceof CategoryUpdatedEvent) {
            return toAvro((CategoryUpdatedEvent) event);
        } else if (event instanceof CategoryDeletedEvent) {
            return toAvro((CategoryDeletedEvent) event);
        }
        throw new IllegalArgumentException("Unsupported event type: " + event.getClass().getName());
    }

    private CategoryEventAvro toAvro(CategoryCreatedEvent event) {
        return CategoryEventAvro.newBuilder()
                .setEventId(event.getEventId().toString())
                .setEventType(event.getEventType())
                .setOccurredOn(event.getOccurredOn().toString())
                .setCategoryId(event.getCategoryId().toString())
                .setOrgId(event.getOrgId().toString())
                .setParentId(event.getParentId() != null ? event.getParentId().toString() : null)
                .setName(event.getName())
                .setDescription(event.getDescription())
                .setMetadata(convertMetadataToStringMap(event.getMetadata()))
                .setIsActive(event.isActive())
                .build();
    }

    private CategoryEventAvro toAvro(CategoryUpdatedEvent event) {
        return CategoryEventAvro.newBuilder()
                .setEventId(event.getEventId().toString())
                .setEventType(event.getEventType())
                .setOccurredOn(event.getOccurredOn().toString())
                .setCategoryId(event.getCategoryId().toString())
                .setOrgId(event.getOrgId().toString())
                .setParentId(event.getParentId() != null ? event.getParentId().toString() : null)
                .setName(event.getName())
                .setDescription(event.getDescription())
                .setMetadata(convertMetadataToStringMap(event.getMetadata()))
                .setIsActive(event.isActive())
                .build();
    }

    private CategoryEventAvro toAvro(CategoryDeletedEvent event) {
        return CategoryEventAvro.newBuilder()
                .setEventId(event.getEventId().toString())
                .setEventType(event.getEventType())
                .setOccurredOn(event.getOccurredOn().toString())
                .setCategoryId(event.getCategoryId().toString())
                .setOrgId(event.getOrgId().toString())
                .setParentId(null)
                .setName("DELETED")
                .setDescription(null)
                .setMetadata(null)
                .setIsActive(false)
                .build();
    }

    private Map<String, String> convertMetadataToStringMap(Map<String, Object> metadata) {
        if (metadata == null) {
            return null;
        }
        return metadata.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() != null ? e.getValue().toString() : ""
                ));
    }
}
