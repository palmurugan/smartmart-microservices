package com.smart.mart.catalog.application.dto.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class CategoryResponse {
    private UUID id;
    private UUID orgId;
    private UUID parentId;
    private String name;
    private String description;
    private Map<String, Object> metadata;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private boolean isActive;
}
