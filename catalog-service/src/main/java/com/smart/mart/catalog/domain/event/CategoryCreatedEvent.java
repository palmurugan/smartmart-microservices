package com.smart.mart.catalog.domain.event;

import com.smart.mart.catalog.domain.model.Category;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class CategoryCreatedEvent extends DomainEvent {
    private final UUID categoryId;
    private final UUID orgId;
    private final UUID parentId;
    private final String name;
    private final String description;
    private final Map<String, Object> metadata;
    private final boolean isActive;

    public CategoryCreatedEvent(Category category) {
        super("CATEGORY_CREATED");
        this.categoryId = category.getId().getValue();
        this.orgId = category.getOrgId().getValue();
        this.parentId = category.getParentId() != null ? category.getParentId().getValue() : null;
        this.name = category.getName();
        this.description = category.getDescription();
        this.metadata = category.getMetadata() != null ? category.getMetadata().getAttributes() : null;
        this.isActive = category.isActive();
    }
}
