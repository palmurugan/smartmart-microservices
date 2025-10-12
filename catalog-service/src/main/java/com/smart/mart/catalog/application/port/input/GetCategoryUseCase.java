package com.smart.mart.catalog.application.port.input;

import com.smart.mart.catalog.application.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface GetCategoryUseCase {
    CategoryResponse getById(UUID id);

    List<CategoryResponse> getByOrgId(UUID orgId);

    List<CategoryResponse> getChildren(UUID parentId);

    List<CategoryResponse> getRootCategories(UUID orgId);
}
