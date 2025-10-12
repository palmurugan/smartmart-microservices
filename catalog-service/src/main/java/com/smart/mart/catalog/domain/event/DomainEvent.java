package com.smart.mart.catalog.domain.event;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class DomainEvent {
    private final UUID eventId;
    private final ZonedDateTime occurredOn;
    private final String eventType;

    protected DomainEvent(String eventType) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = ZonedDateTime.now();
        this.eventType = eventType;
    }

    public UUID getEventId() {
        return eventId;
    }

    public ZonedDateTime getOccurredOn() {
        return occurredOn;
    }

    public String getEventType() {
        return eventType;
    }
}
