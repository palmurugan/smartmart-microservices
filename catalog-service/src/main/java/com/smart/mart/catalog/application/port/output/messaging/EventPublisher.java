package com.smart.mart.catalog.application.port.output.messaging;

import com.smart.mart.catalog.domain.event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
