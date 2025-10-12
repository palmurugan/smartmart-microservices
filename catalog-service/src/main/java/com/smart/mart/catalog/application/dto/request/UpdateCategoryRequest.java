package com.smart.mart.catalog.application.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class UpdateCategoryRequest {

    private UUID parentId;

    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private Map<String, Object> metadata;

    private Boolean isActive;
}
