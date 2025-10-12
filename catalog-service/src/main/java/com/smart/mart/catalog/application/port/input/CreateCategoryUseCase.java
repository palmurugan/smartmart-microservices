package com.smart.mart.catalog.application.port.input;

import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;

public interface CreateCategoryUseCase {
    CategoryResponse create(CreateCategoryRequest request);
}
