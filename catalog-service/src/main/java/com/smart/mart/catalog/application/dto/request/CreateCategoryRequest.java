package com.smart.mart.catalog.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class CreateCategoryRequest {

    @NotNull(message = "Organization ID cannot be null")
    private UUID orgId;

    private UUID parentId;

    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 255, message = "Category name cannot be more than 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private Map<String, Object> metadata;

}
