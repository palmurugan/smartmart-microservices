package com.smart.mart.catalog.application.port.input;

import com.smart.mart.catalog.application.dto.request.UpdateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;

import java.util.UUID;

public interface UpdateCategoryUseCase {
    CategoryResponse update(UUID id, UpdateCategoryRequest request);
}
