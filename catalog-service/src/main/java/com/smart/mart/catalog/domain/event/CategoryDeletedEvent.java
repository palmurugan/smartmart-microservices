package com.smart.mart.catalog.domain.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CategoryDeletedEvent extends DomainEvent {
    private final UUID categoryId;
    private final UUID orgId;

    public CategoryDeletedEvent(UUID categoryId, UUID orgId) {
        super("CATEGORY_DELETED");
        this.categoryId = categoryId;
        this.orgId = orgId;
    }
}
