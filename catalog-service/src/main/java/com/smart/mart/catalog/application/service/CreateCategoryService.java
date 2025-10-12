package com.smart.mart.catalog.application.service;

import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.mapper.CategoryMapper;
import com.smart.mart.catalog.application.port.input.CreateCategoryUseCase;
import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.config.annotation.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateCategoryService implements CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {
        if (request.getParentId() != null) {
            validateParentExists(request.getParentId());
        }

        // Map DTO to Domain
        Category category = categoryMapper.toDomain(request);

        // Domain validation happens automatically in Category constructor
        Category savedCategory = categoryRepository.save(category);

        // Map Domain to Response
        return categoryMapper.toResponse(savedCategory);
    }

    private void validateParentExists(java.util.UUID parentId) {
        if (!categoryRepository.existsById(new CategoryId(parentId))) {
            throw new CategoryNotFoundException("Parent category not found with id: " + parentId);
        }
    }
}
